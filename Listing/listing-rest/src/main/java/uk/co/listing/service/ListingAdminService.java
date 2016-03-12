package uk.co.listing.service;

import static uk.co.listing.rest.message.MessageBundler.getMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.co.listing.dao.CourtDiaryDao;
import uk.co.listing.dao.CourtRoomDao;
import uk.co.listing.dao.CourtRoomInDiaryDao;
import uk.co.listing.dao.CourtSessionDao;
import uk.co.listing.dao.JudicialOfficerDao;
import uk.co.listing.dao.PanelDao;
import uk.co.listing.dao.PanelMemberDao;
import uk.co.listing.dao.SessionBlockDao;
import uk.co.listing.dao.SittingDao;
import uk.co.listing.domain.CourtDiary;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtRoomInDiary;
import uk.co.listing.domain.CourtSession;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.Panel;
import uk.co.listing.domain.PanelMember;
import uk.co.listing.domain.SessionBlock;
import uk.co.listing.domain.SittingDate;
import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.JudicialTicketsEnum;
import uk.co.listing.domain.constant.PanelMemberType;
import uk.co.listing.domain.constant.SessionBlockType;
import uk.co.listing.exceptions.CcsException;
import uk.co.listing.rest.response.CourtRoomWeb;
import uk.co.listing.rest.response.CourtSessionSaveWeb;
import uk.co.listing.rest.response.JudicialOfficerWeb;
import uk.co.listing.rest.response.ManageSessionAction;
import uk.co.listing.utils.DateTimeUtils;

@Service("listingAdminService")
@Transactional(readOnly = true)
public class ListingAdminService implements IListingAdminService {

    private static final Logger LOGGER = Logger.getLogger(HearingSlotService.class);

    @Autowired
    private CourtRoomDao courtRoomDao;

    @Autowired
    private CourtDiaryDao courtDiaryDao;

    @Autowired
    private CourtRoomInDiaryDao courtRoomInDiaryDao;

    @Autowired
    private JudicialOfficerDao judicialOfficerDao;

    @Autowired
    private PanelDao panelDao;

    @Autowired
    private PanelMemberDao panelMemberDao;

    @Autowired
    private SittingDao sittingDao;

    @Autowired
    private CourtSessionDao courtSessionDao;

    @Autowired
    private SessionBlockDao sessionBlockDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CourtSessionSaveWeb allocateJudgeToCourtRoom(final CourtSessionSaveWeb courtSessionWeb) {
        Date startDay = DateTimeUtils.getBeginningOfTheDay(courtSessionWeb.getSittingStartDate());
        final Date endDay = DateTimeUtils.getEndOfTheDay(courtSessionWeb.getSittingEndDate());

        DateTimeUtils.checkDates(startDay, endDay);
        final JudicialOfficer judicialOfficer = judicialOfficerDao.findJudicialOfficerByName(courtSessionWeb.getJudgeName());
        if (judicialOfficer == null) {
            throw new CcsException("Judicial Officer " + courtSessionWeb.getJudgeName() + getMessage("DOES_NOT_EXIST_IN_THE_SYSTEM"));
        }
        final CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(courtSessionWeb.getCourtRoomName());
        if (courtRoom == null) {
            throw new CcsException("Court room " + courtSessionWeb.getCourtRoomName() + getMessage("DOES_NOT_EXIST_IN_THE_SYSTEM"));
        }

        while (!startDay.after(endDay)) {
            if (!DateTimeUtils.isWeekend(startDay)) {
                final List<CourtSession> courtSessions = courtSessionDao.findOpenAndClosedSessionsForSittings(courtRoom.getId(), startDay, startDay);
                populateCourtSession(courtSessionWeb, startDay, judicialOfficer, courtRoom, courtSessions);

            }
            startDay = DateUtils.addDays(startDay, 1);
        }
        LOGGER.info("Judicial officer allcated to room successfully");
        return courtSessionWeb;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CourtSessionSaveWeb deallocateJudgeFromCourtRoom(final CourtSessionSaveWeb courtSessionWeb) {
        final Date endDay = DateTimeUtils.getEndOfTheDay(courtSessionWeb.getSittingEndDate());
        Date startDay = DateTimeUtils.getBeginningOfTheDay(courtSessionWeb.getSittingStartDate());
        DateTimeUtils.checkDates(startDay, endDay);

        final JudicialOfficer judicialOfficer = judicialOfficerDao.findJudicialOfficerByName(courtSessionWeb.getJudgeName());
        if (judicialOfficer == null) {
            throw new CcsException("Judicial Officer " + courtSessionWeb.getJudgeName() + getMessage("DOES_NOT_EXIST_IN_THE_SYSTEM"));
        }
        final CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(courtSessionWeb.getCourtRoomName());
        if (courtRoom == null) {
            throw new CcsException("Court room " + courtSessionWeb.getCourtRoomName() + getMessage("DOES_NOT_EXIST_IN_THE_SYSTEM"));
        }

        while (!startDay.after(endDay)) {
            if (!DateTimeUtils.isWeekend(startDay)) {
                populateCourtSession(startDay, judicialOfficer, courtRoom);
            }
            startDay = DateUtils.addDays(startDay, 1);
        }

        courtSessionWeb.setSuccessMessage(getMessage("JUDGE_NO_LONGER_ALLOCATED_TO_THESE_SESSIONS"));
        LOGGER.info("Judicial officer de allocated successfully");
        return courtSessionWeb;

    }

    @Override
    public List<JudicialOfficerWeb> findAllJudicialOfficers() {
        final List<JudicialOfficer> listJudicialOfficer = judicialOfficerDao.findAll();
        final List<JudicialOfficerWeb> listJudicialOfficerWeb = new ArrayList<JudicialOfficerWeb>();
        for (final JudicialOfficer judicialOfficer : listJudicialOfficer) {
            final JudicialOfficerWeb judicialOfficerWeb = new JudicialOfficerWeb(judicialOfficer.getFullName(), judicialOfficer.getJudicialOfficerType().getDescription());
            setJudicialTicketsWeb(judicialOfficer.getJudicialTickets(), judicialOfficerWeb.getJudicialTickets());
            judicialOfficerWeb.setQc(judicialOfficer.getIsQC() ? "Yes" : "No");
            if (judicialOfficer.getJudicialOfficerType().equals(JudicialOfficerType.CIRCUIT)) {
                judicialOfficerWeb.setResident(judicialOfficer.getIsResident() ? "Yes" : "No");
            }
            listJudicialOfficerWeb.add(judicialOfficerWeb);
        }
        return listJudicialOfficerWeb;
    }

    private void setJudicialTicketsWeb(final Set<JudicialTicketsEnum> judicialTickets, final Set<String> judicialTicketsWeb) {
        if (!judicialTickets.isEmpty()) {
            judicialTickets.forEach(ticket -> judicialTicketsWeb.add(ticket.getDescription()));
        }

    }

    @Override
    public JudicialOfficerWeb findJudicialOfficerByName(final String name) {
        final JudicialOfficer judicialOfficer = judicialOfficerDao.findJudicialOfficerByName(name);
        if (judicialOfficer == null) {
            return null;
        }
        final JudicialOfficerWeb judicialOfficerWeb = new JudicialOfficerWeb(judicialOfficer.getFullName(), judicialOfficer.getJudicialOfficerType().getDescription());
        return judicialOfficerWeb;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ManageSessionAction manageCourtSession(final ManageSessionAction manageSessionAction) {
        final Date startDay = DateTimeUtils.getBeginningOfTheDay(manageSessionAction.getSittingStartDate());
        final Date endDay = DateTimeUtils.getEndOfTheDay(manageSessionAction.getSittingEndDate());

        DateTimeUtils.checkDates(startDay, endDay);
        if (manageSessionAction.getCourtRoomNames() == null) {
            throw new CcsException(getMessage("NO_COURT_ROOM_SELECTED"));
        }
        for (final String courtRoomName : manageSessionAction.getCourtRoomNames()) {
            final CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(courtRoomName);
            if (courtRoom == null) {
                throw new CcsException("Court room " + courtRoomName + getMessage("DOES_NOT_EXIST_IN_THE_SYSTEM"));
            }

            final List<CourtSession> courtSessions = courtSessionDao.findOpenAndClosedSessionsForSittings(courtRoom.getId(), startDay, endDay);
            for (final CourtSession courtSession : courtSessions) {
                courtSession.setIsClosed(manageSessionAction.getClosed());
                courtSessionDao.save(courtSession);
            }

        }
        LOGGER.info("Court Session updated successfully");
        return manageSessionAction;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveCourtRoom(final CourtRoomWeb courtRoomWeb) {
        final CourtDiary courtDiaries = courtDiaryDao.findCourtDiary(courtRoomWeb.getCourtCenterName());
        final CourtRoomInDiary roomInDiary = new CourtRoomInDiary();
        final CourtRoom courtRoom = new CourtRoom();
        courtRoom.setRoomName(courtRoomWeb.getCourtRoomName());
        courtRoomDao.save(courtRoom);
        roomInDiary.setCourtRoom(courtRoom);
        roomInDiary.setCourtDiary(courtDiaries);
        courtRoomInDiaryDao.save(roomInDiary);
        LOGGER.info("Court room saved successfully");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveJudicialOfficer(final JudicialOfficerWeb judicialOfficerWeb) {
        final JudicialOfficer judicialOfficer = new JudicialOfficer();
        final String fullname = judicialOfficerWeb.getFullname();
        if (StringUtils.isBlank(fullname) || !fullname.trim().matches("^(([a-zA-Z.\\-|'])+\\s{0,1})*$")) {
            throw new CcsException("Invalid name: " + fullname);
        }
        if (findJudicialOfficerByName(fullname.trim()) != null) {
            throw new CcsException(fullname + " already exists.");
        }
        judicialOfficer.setFullName(fullname.trim());
        judicialOfficer.setJudicialOfficerType(JudicialOfficerType.getJudicialOfficerType(judicialOfficerWeb.getType()));
        judicialOfficer.setIsQC(judicialOfficerWeb.getIsQC());
        setJudicialTickets(judicialOfficerWeb.getJudicialTickets(), judicialOfficer.getJudicialTickets());
        if (JudicialOfficerType.getJudicialOfficerType(judicialOfficerWeb.getType()).equals(JudicialOfficerType.CIRCUIT)) {
            judicialOfficer.setIsResident(judicialOfficerWeb.getIsResident());
        } else {
            judicialOfficer.setIsResident(false);
        }
        judicialOfficerDao.save(judicialOfficer);
        LOGGER.info("Judicial officer saved successfully");

    }

    private void setJudicialTickets(final Set<String> judicialTickets, final Set<JudicialTicketsEnum> judicialTicketsEnum) {
        if (!judicialTickets.isEmpty()) {
            judicialTickets.forEach(ticket -> judicialTicketsEnum.add(JudicialTicketsEnum.getJudicialTicketsType(ticket)));
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CourtSessionSaveWeb setSessionBlockWithType(final CourtSessionSaveWeb courtSessionWeb, final boolean deleteBlocks) {
        Date startDay = DateTimeUtils.getBeginningOfTheDay(courtSessionWeb.getSittingStartDate());
        final Date endDay = DateTimeUtils.getEndOfTheDay(courtSessionWeb.getSittingEndDate());

        DateTimeUtils.checkDates(startDay, endDay);
        final CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(courtSessionWeb.getCourtRoomName());
        if (courtRoom == null) {
            throw new CcsException("Court room " + courtSessionWeb.getCourtRoomName() + getMessage("DOES_NOT_EXIST_IN_THE_SYSTEM"));
        }

        while (!startDay.after(endDay)) {
            if (!DateTimeUtils.isWeekend(startDay)) {
                final List<CourtSession> courtSessions = courtSessionDao.findOpenCourtRoomSittingByRoomNameBetweenDates(courtRoom.getId(), startDay, DateTimeUtils.getEndOfTheDay(startDay));

                if (deleteBlocks) {
                    courtSessions.forEach(courtsession -> courtsession.getSessionBlock().forEach(sessionBlock -> validateSessionBlockType(courtSessionWeb, sessionBlock)));
                } else {
                    populateCourtSession(courtSessionWeb, startDay, courtRoom, courtSessions);
                }

            }
            startDay = DateUtils.addDays(startDay, 1);
        }
        LOGGER.info("Blocks created successfully");
        return courtSessionWeb;

    }

    private void handleCourtSession(final CourtSessionSaveWeb courtSessionWeb, final JudicialOfficer judicialOfficer, final CourtSession session) {
        if ((session.getIsClosed() == null) || !session.getIsClosed()) {
            final Panel panel = session.getPanel();
            boolean judgeInTheSession = false;
            for (final PanelMember member : panel.getPanelMember()) {
                if (member.getJudicialOfficer().getId().equals(judicialOfficer.getId())) {
                    courtSessionWeb.setErrorMessage(getMessage("JUDGE_ALREADY_ALLOCATED", new String[] { "${JUDGEFULNAME}" }, new String[] { judicialOfficer.getFullName() }));
                    judgeInTheSession = true;
                }
            }
            if (!judgeInTheSession) {
                final PanelMember panelMember = new PanelMember();
                panelMember.setPanel(session.getPanel());
                panelMember.setJudicialOfficer(judicialOfficer);
                panelMember.setPanelMemberType(PanelMemberType.JUDGE);
                panelMemberDao.save(panelMember);
            }
        }
    }

    private void populateCourtSession(final CourtSessionSaveWeb courtSessionWeb, final Date startDay, final CourtRoom courtRoom, final List<CourtSession> courtSessions) {
        CourtSession session;
        if (courtSessions.isEmpty()) {
            final Panel panel = new Panel();
            panel.setDescription("Panel for " + courtSessionWeb.getBlockType());
            panelDao.save(panel);

            final SittingDate sitting = new SittingDate();
            sitting.setCourtRoom(courtRoom);
            sitting.setDay(DateTimeUtils.getBeginningOfTheDay(startDay));
            sittingDao.save(sitting);

            session = new CourtSession();
            session.setSitting(sitting);
            session.setPanel(panel);
            courtSessionDao.save(session);

        } else {
            session = courtSessions.get(0);

        }
        final SessionBlock sessionBlock = new SessionBlock();
        sessionBlock.setBlockType(SessionBlockType.getSessionBlockType(courtSessionWeb.getBlockType()));
        sessionBlock.setSession(session);
        sessionBlockDao.save(sessionBlock);
    }

    private void populateCourtSession(final CourtSessionSaveWeb courtSessionWeb, final Date startDay, final JudicialOfficer judicialOfficer, final CourtRoom courtRoom,
            final List<CourtSession> courtSessions) {
        if (courtSessions.isEmpty()) {
            final Panel panel = new Panel();
            panel.setDescription("Panel for " + courtSessionWeb.getJudgeName());
            panelDao.save(panel);

            final PanelMember panelMember = new PanelMember();
            panelMember.setPanel(panel);
            panelMember.setJudicialOfficer(judicialOfficer);
            panelMember.setPanelMemberType(PanelMemberType.JUDGE);
            panelMemberDao.save(panelMember);

            final SittingDate sitting = new SittingDate();
            sitting.setCourtRoom(courtRoom);
            sitting.setDay(startDay);
            sittingDao.save(sitting);

            final CourtSession session = new CourtSession();
            session.setSitting(sitting);
            session.setPanel(panel);
            session.setIsClosed(false);
            courtSessionDao.save(session);

            final SessionBlock sessionBlock = new SessionBlock();
            sessionBlock.setBlockType(SessionBlockType.TRIAL);
            sessionBlock.setSession(session);
            sessionBlockDao.save(sessionBlock);
        } else {
            for (final CourtSession session : courtSessions) {
                handleCourtSession(courtSessionWeb, judicialOfficer, session);
            }
        }
    }

    private void populateCourtSession(final Date startDay, final JudicialOfficer judicialOfficer, final CourtRoom courtRoom) {
        final List<CourtSession> courtSessions = courtSessionDao.findOpenCourtRoomSittingByRoomNameBetweenDates(courtRoom.getId(), startDay, DateTimeUtils.getEndOfTheDay(startDay));
        if (CollectionUtils.isNotEmpty(courtSessions)) {
            final CourtSession session = courtSessions.get(0);
            final Panel panel = session.getPanel();
            for (final PanelMember member : panel.getPanelMember()) {
                if (member.getJudicialOfficer().getId().equals(judicialOfficer.getId())) {
                    panelMemberDao.delete(member);
                }
            }
        }
    }

    private void validateSessionBlockType(final CourtSessionSaveWeb courtSessionWeb, final SessionBlock sessionBlock) {
        if (sessionBlock.getBlockType().equals(SessionBlockType.getSessionBlockType(courtSessionWeb.getBlockType()))) {
            if (CollectionUtils.isEmpty(sessionBlock.getHearings())) {
                sessionBlockDao.delete(sessionBlock);
            } else {
                throw new CcsException(getMessage("ONE_OF_THE_BLOCK_CONTAINS_HEARING"));
            }

        }
    }

}
