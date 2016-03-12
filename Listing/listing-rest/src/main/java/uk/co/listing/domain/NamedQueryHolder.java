package uk.co.listing.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Just a placeholder to keep all your named queries at a central place rather
 * than getting scattered all over the Java code. Keep adding all the named
 * queries to this entity declaration.
 *
 * @author rvinayak
 *
 */

// Adding 1 second to the sitting day start due to an apparent bug in
// hibernate/postgres where the lower limit of a range is exclusive instead of
// inclusive
@NamedNativeQueries({
        @NamedNativeQuery(name = "countActualSittingDays", query = "SELECT count(distinct ( Judicialofficer.fullname, sittingdate.day) ) FROM"
                + " courtcenter, courtdiary, courtroomindiary, courtroom, courtsession, sittingdate, panel, "
                + " panelmember, judicialofficer WHERE courtdiary.courtcenterid = courtcenter.id AND courtroomindiary.courtdiaryid = courtdiary.id AND "
                + " courtroomindiary.roomid = courtroom.id AND sittingdate.courtroomid = courtroom.id AND courtsession.sittingid = sittingdate.id AND ( courtsession.isClosed='N' or courtsession.isClosed is null ) AND"
                + " courtsession.panelid = panel.id AND panelmember.panelid = panel.id AND panelmember.judicialofficerid = judicialofficer.id "
                + " and (sittingdate.day + INTERVAL '1 second')>=? and sittingdate.day<=? and courtcenter.centername = ? "),
        @NamedNativeQuery(name = "findAuditedPerson", query = "SELECT count(*) FROM person_aud where personfullname = ? "),
        @NamedNativeQuery(name = "deleteCrestdefendant", query = "delete from personincase where roleincase ='DEFENDANT' and caseid in (select id from caserelated where caseclosed ='N' and fromcrest='Y')"),
        @NamedNativeQuery(name = "deleteCrestNonAvail", query = "delete from CrustNonAvailableDates where caseid in (select id from caserelated where caseclosed ='N' and fromcrest='Y')"),
        @NamedNativeQuery(name = "deleteCrestHeraingInstance", query = "delete from hearinginstance  where hearingid  in (select id from hearing where startdate >= current_date and hearingtype= 'PCM' and caseid in( select id from caserelated where caseclosed ='N' and fromcrest='Y'))"),
        @NamedNativeQuery(name = "deleteCrestHearing", query = "delete from hearing where startdate >= current_date and hearingtype= 'PCM' and caseid in( select id from caserelated where caseclosed ='N' and fromcrest='Y')"),
        @NamedNativeQuery(name = "deleteCrestLinkedCase", query = "delete from linkedcases  where casedrelatedid  in (select id from caserelated where caseclosed ='N' and fromcrest='Y')"),
        @NamedNativeQuery(name = "deleteCrestNotes", query = "delete from casenote where caseid in (select id from caserelated where caseclosed ='N' and fromcrest='Y')") })
@NamedQueries({
        @NamedQuery(name = "findCaseByCrestCaseNumber", query = "SELECT caseRelated from CaseRelated caseRelated LEFT JOIN caseRelated.hearings hear WITH (hear.active='Y') LEFT JOIN FETCH caseRelated.notes LEFT JOIN FETCH "
                + "caseRelated.linkedCases LEFT JOIN FETCH  caseRelated.personInCase pic LEFT JOIN FETCH  pic.person LEFT JOIN FETCH  caseRelated.crustNonAvailableDatesList where UPPER(caseRelated.crestCaseNumber) = UPPER(?) "),

        @NamedQuery(name = "findCaseByCrestCaseNumberAndCenter", query = "SELECT caseRelated from CaseRelated caseRelated LEFT JOIN caseRelated.hearings hear WITH (hear.active='Y') LEFT JOIN FETCH caseRelated.notes LEFT JOIN FETCH "
                + "caseRelated.linkedCases LEFT JOIN FETCH  caseRelated.personInCase pic LEFT JOIN FETCH  pic.person LEFT JOIN FETCH  caseRelated.crustNonAvailableDatesList where UPPER(caseRelated.crestCaseNumber) = UPPER(?) "
                + "and caseRelated.courtCenter.centerName = (?)"),

        @NamedQuery(name = "findCourtDiaryByCenterName", query = " from CourtDiary c where center.centerName = :centerName "),

        @NamedQuery(name = "findAvailableSessionBlockDates", query = "SELECT cr.roomName, sd.day, 0 "
                + "FROM SessionBlock sb, CourtSession cs, SittingDate sd, CourtRoom cr, CourtCenter cc, CourtDiary cd, CourtRoomInDiary crid, PanelMember pm, Panel p " + "WHERE sb.session = cs AND ( cs.isClosed='N' or cs.isClosed is null )" 
                + "AND cs.sitting = sd " + "AND sd.courtRoom = cr " + " AND sd.day between :dateStart and :dateEnd " + "AND sb.blockType = :blockType " + "AND cc.centerName = :courtCenterName " + "AND cd.center = cc "
                + "AND crid.courtDiary = cd " + "AND pm.panel = p " + "AND cs.panel = p " + "AND crid.courtRoom = cr " + "AND sb NOT IN (SELECT DISTINCT hi.block from HearingInstance hi) "
                + "AND NOT EXISTS (select 1 from CrustNonAvailableDates cnad where cnad.caseRelated = :caseRelated  and sd.day between cnad.startDate and cnad.endDate) ORDER BY sd.day ASC"),

        @NamedQuery(name = "findAvailableSessionBlockDatesWithOverbooking", query = "SELECT cr.roomName, sd.day, count(HearingInstance.id) as overbookCount "
                + "FROM SessionBlock sb LEFT OUTER JOIN sb.hearings HearingInstance, CourtSession cs, CourtRoom cr, SittingDate sd, CourtCenter cc, CourtDiary cd, CourtRoomInDiary crid, PanelMember pm, Panel p "
                        + "WHERE sb.session = cs AND ( cs.isClosed='N' or cs.isClosed is null ) AND cs.sitting = sd AND sd.courtRoom = cr AND sd.day between :dateStart and :dateEnd "
                + "AND sb.blockType = :blockType AND cc.centerName = :courtCenterName AND cd.center = cc AND crid.courtDiary = cd AND crid.courtRoom = cr AND cs.panel = p AND pm.panel = p "
                + "AND NOT EXISTS (select 1 from CrustNonAvailableDates cnad where cnad.caseRelated = :caseRelated  and sd.day between cnad.startDate and cnad.endDate)"
                + "GROUP BY cr.roomName, sd.day HAVING count(HearingInstance.id) < :overbookCount ORDER BY overbookCount, sd.day"),
        @NamedQuery(name = "findAvailableSessionBlockDatesWithJudge", query = "SELECT cr.roomName, sd.day, 0 "
                + "FROM SessionBlock sb LEFT OUTER JOIN sb.hearings HearingInstance, CourtSession cs, SittingDate sd, CourtRoom cr, CourtCenter cc, CourtDiary cd, CourtRoomInDiary crid, PanelMember pm, Panel p "
                + "WHERE sb.session = cs AND ( cs.isClosed='N' or cs.isClosed is null )" + "AND cs.sitting = sd " + "AND sd.courtRoom = cr " + " AND sd.day between :dateStart and :dateEnd "
                + "AND sb.blockType = :blockType " + "AND cc.centerName = :courtCenterName " + "AND cd.center = cc " + "AND crid.courtDiary = cd " + "AND crid.courtRoom = cr " + "AND pm.panel = p "
                + "AND cs.panel = p " + "AND pm.judicialOfficer = :judge " + "AND sb NOT IN (SELECT DISTINCT hi.block from HearingInstance hi) "
                + "AND NOT EXISTS (select 1 from CrustNonAvailableDates cnad where cnad.caseRelated = :caseRelated  and sd.day between cnad.startDate and cnad.endDate) ORDER BY sd.day ASC"),

        @NamedQuery(name = "findAvailableSessionBlockDatesWithOverbookingAndJudge", query = "SELECT cr.roomName, sd.day, count(HearingInstance.id) as overbookCount "
                + "FROM SessionBlock sb LEFT OUTER JOIN sb.hearings HearingInstance, CourtSession cs, CourtRoom cr, SittingDate sd, CourtCenter cc, CourtDiary cd, CourtRoomInDiary crid, PanelMember pm, Panel p "
                + "WHERE sb.session = cs AND ( cs.isClosed='N' or cs.isClosed is null ) AND cs.sitting = sd AND sd.courtRoom = cr AND sd.day between :dateStart and :dateEnd "
                + "AND sb.blockType = :blockType AND cc.centerName = :courtCenterName AND cd.center = cc AND crid.courtDiary = cd AND crid.courtRoom = cr AND cs.panel = p AND pm.panel = p "
                + "AND pm.judicialOfficer = :judge AND NOT EXISTS (select 1 from CrustNonAvailableDates cnad where cnad.caseRelated = :caseRelated  and sd.day between cnad.startDate and cnad.endDate)"
                + "GROUP BY cr.roomName, sd.day HAVING count(HearingInstance.id) < :overbookCount ORDER BY overbookCount, sd.day"),

        @NamedQuery(name = "findCourtRoomsByCenterName", query = "select crd.courtRoom from CourtRoomInDiary crd" + " JOIN crd.courtDiary cd JOIN cd.center c where c.centerName = :centerName "),

        @NamedQuery(name = "findCourtRoomsByName", query = "from CourtRoom  where roomName = :roomName "),

        @NamedQuery(name = "findSessionByCourtRoomAndSittingBetweenDates", query = "SELECT cs FROM CourtSession cs JOIN cs.panel p JOIN cs.sitting s JOIN s.courtRoom cr where s.day BETWEEN :startDate AND :endDate AND cr.id = :courtRoomId and ( cs.isClosed='N' or cs.isClosed is null )"),
        @NamedQuery(name = "findOpenAndClosedSitting", query = "SELECT cs FROM CourtSession cs JOIN cs.sitting s JOIN s.courtRoom cr where s.day BETWEEN :startDate AND :endDate AND cr.id = :courtRoomId"),
        @NamedQuery(name = "findHearingByKey", query = " from  Hearing where hearingKey=? and active='Y'"),

        @NamedQuery(name = "findUnlistedHearing", query = " from Hearing where bookingStatus = 'NOTBOOKED' and active='Y'"),

        @NamedQuery(name = "findJudicialOfficerByName", query = "from JudicialOfficer where UPPER(fullname)=UPPER(:name)"),

        @NamedQuery(name = "findSessionBlocksByDateAndRoom", query = "SELECT sessionblock  FROM SessionBlock  sessionblock LEFT OUTER JOIN sessionblock.session courtsession LEFT OUTER JOIN "
                + "courtsession.sitting sittingdate LEFT OUTER JOIN  sittingdate.courtRoom room  where room.roomName=:courtRoomName and  ( courtsession.isClosed='N' or courtsession.isClosed is null ) and sittingdate.day in :sittingdate and sessionblock.blockType = :sessionblockType "),

        @NamedQuery(name = "findSessionBlocksByDateCourtCenterAndType", query = "SELECT sb  "
                + " FROM SessionBlock sb, CourtSession cs, SittingDate sd, CourtRoom cr, CourtCenter cc, CourtDiary cd, CourtRoomInDiary crid "
                + " WHERE sb.session = cs AND  ( cs.isClosed='N' or cs.isClosed is null ) " + "AND cs.sitting = sd  AND sd.courtRoom = cr  AND cd.center = cc AND crid.courtDiary = cd " + "AND crid.courtRoom = cr "
                + " AND sd.day = :sittingdate   AND sb.blockType = :sessionblockType AND cc.centerName = :courtCenterName  "),

        @NamedQuery(name = "findHearingCourtRoom", query = "select DISTINCT courtroom from Hearing hearing JOIN hearing.hearingInstance hearinginstance JOIN hearinginstance.block sessionblock JOIN sessionblock.session courtsession JOIN courtsession.sitting sittingdate JOIN  sittingdate.courtRoom courtroom where  hearing.hearingKey= ?"),

        @NamedQuery(name = "findPersonInCaseByCrestCaseNumberAndPersonName", query = "SELECT personInCase FROM PersonInCase personInCase LEFT JOIN personInCase.caseRelated caseRelated LEFT JOIN FETCH  personInCase.person WHERE UPPER(personInCase.caseRelated.crestCaseNumber) = UPPER(?) "
                + "AND UPPER(personInCase.person.personFullName) = UPPER(?)"),
        @NamedQuery(name = "findNoteByCrestCaseNumberAndDescription", query = "SELECT caseNote FROM CaseNote caseNote LEFT JOIN caseNote.caseRelated caseRelated WHERE UPPER(caseNote.caseRelated.crestCaseNumber) = UPPER(?) "
                + "AND caseNote.id = ?"),
        @NamedQuery(name = "findTaskByProcessingStatus", query = " from  CrestDataBatchJob where processingState = ? order by batchId "),
        @NamedQuery(name = "getCreateDataByBatchId", query = " from  CrestData where batchId = ? "),
        @NamedQuery(name = "getCrestDataBatchJobById", query = " from  CrestDataBatchJob where batchId = ? "),
        @NamedQuery(name = "getCreateDataForInsert", query = " from CrestData crestData  where crestData.crestDataBatchJob.batchId= ? and crestData.caseNumber not in  (SELECT distinct caseRelated.crestCaseNumber FROM CaseRelated caseRelated WHERE caseRelated.fromCrest='Y' ) "),
        @NamedQuery(name = "getCreateDataForCompletion", query = " from CaseRelated caseRelated  where caseRelated.fromCrest='Y' and caseRelated.crestCaseNumber  not in (SELECT distinct crestData.caseNumber  FROM CrestData crestData WHERE crestData.crestDataBatchJob.batchId=? ) "),
        @NamedQuery(name = "getCreateDataForUpdate", query = " from CrestData crestData  where crestData.crestDataBatchJob.batchId= ? and crestData.caseNumber  in  (SELECT distinct caseRelated.crestCaseNumber FROM CaseRelated caseRelated WHERE caseRelated.fromCrest='Y' ) "),
        @NamedQuery(name = "getInvalidCrestdefendant", query = "from CrestDefendant crestDefendant where crestDefendant.crestDataBatchJob.batchId =? and crestDefendant.caseNumber not in(SELECT distinct caseRelated.crestCaseNumber FROM CaseRelated caseRelated WHERE caseRelated.fromCrest='Y' and caseRelated.caseClosed ='N')"),
        @NamedQuery(name = "getCrestdefendant", query = "from CrestDefendant crestDefendant where crestDefendant.crestDataBatchJob.batchId =?"),
        @NamedQuery(name = "getInvalidCrestNotes", query = "from CrestNote crestNote where crestNote.crestDataBatchJob.batchId =? and crestNote.caseNumber not in(SELECT distinct caseRelated.crestCaseNumber FROM CaseRelated caseRelated WHERE caseRelated.fromCrest='Y' and caseRelated.caseClosed ='N')"),
        @NamedQuery(name = "getCrestNotes", query = "from CrestNote crestNote where crestNote.crestDataBatchJob.batchId =?"),
        @NamedQuery(name = "getInvalidCrestNonAvail", query = "from CrestNonAvailable crestNonAvail where crestNonAvail.crestDataBatchJob.batchId =? and crestNonAvail.caseNumber not in(SELECT distinct caseRelated.crestCaseNumber FROM CaseRelated caseRelated WHERE caseRelated.fromCrest='Y' and caseRelated.caseClosed ='N')"),
        @NamedQuery(name = "getCrestNonAvail", query = "from CrestNonAvailable crestNonAvail where crestNonAvail.crestDataBatchJob.batchId =?"),
        @NamedQuery(name = "getInvalidCrestHearing", query = "from CrestPcmh crestPcmh where crestPcmh.crestDataBatchJob.batchId =? and crestPcmh.caseNumber not in(SELECT distinct caseRelated.crestCaseNumber FROM CaseRelated caseRelated WHERE caseRelated.fromCrest='Y' and caseRelated.caseClosed ='N')"),
        @NamedQuery(name = "getCrestHearing", query = "from CrestPcmh crestPcmh where crestPcmh.crestDataBatchJob.batchId =?"),
        @NamedQuery(name = "getInvalidCrestLinkedcases", query = "from CrestLinkedCase crestLinkedCase where crestLinkedCase.crestDataBatchJob.batchId =? and crestLinkedCase.caseNumber not in(SELECT distinct caseRelated.crestCaseNumber FROM CaseRelated caseRelated WHERE caseRelated.fromCrest='Y' and caseRelated.caseClosed ='N') or crestLinkedCase.linkedCases not in(SELECT distinct caseRelated.crestCaseNumber FROM CaseRelated caseRelated)"),
        @NamedQuery(name = "getCrestLinkedCases", query = "from CrestLinkedCase crestLinkedCase where crestLinkedCase.crestDataBatchJob.batchId =?"),
        @NamedQuery(name = "findPcmHearingForTomorrow", query = " from  Hearing h where  date(h.startDate) = :startDate and h.hearingStatus= :stauts and h.hearingType ='PCM'"),
        @NamedQuery(name = "findPcmHearingForToday", query = " from  Hearing h where date(h.startDate)= :startDate and h.hearingStatus!= :stauts and h.hearingType ='PCM'"),
        @NamedQuery(name = "findPcmHearingForPast", query = " from  Hearing h where date(h.startDate) < :startDate and h.hearingStatus!= :stauts and h.hearingType ='PCM'"),
        @NamedQuery(name = "getProcessingStatus", query = " from CrestDataBatchJob where date(lastModifiedOn) = current_date order by batchId ")

})
@Entity
public class NamedQueryHolder {

    @Id
    private long id;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }
}
