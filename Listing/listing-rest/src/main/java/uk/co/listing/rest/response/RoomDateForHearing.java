package uk.co.listing.rest.response;

import java.util.Date;

import javax.annotation.Generated;

public class RoomDateForHearing implements Comparable<RoomDateForHearing>{
    
    private String roomName;
    private Date slotDate;
    private Long hearingCount;
    
    public RoomDateForHearing(String roomName, Date slotDate, Long hearingCount) {
        super();
        this.roomName = roomName;
        this.slotDate = slotDate;
        this.hearingCount = hearingCount;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(Date slotDate) {
        this.slotDate = slotDate;
    }

    public Long getHearingCount() {
        return hearingCount;
    }

    public void setHearingCount(Long hearingCount) {
        this.hearingCount = hearingCount;
    }

    @Override
    @Generated(value = { "eclipse.generated" })
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((roomName == null) ? 0 : roomName.hashCode());
        result = prime * result + ((slotDate == null) ? 0 : slotDate.hashCode());
        return result;
    }

    @Override
    @Generated(value = { "eclipse.generated" })
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return handleEquals(obj);
    }

    private boolean handleEquals(Object obj) {
        RoomDateForHearing other = (RoomDateForHearing) obj;
        if (roomName == null) {
            if (other.roomName != null)
                return false;
        } else if (!roomName.equals(other.roomName))
            return false;
        return equalsAux(other);
    }

    private boolean equalsAux(RoomDateForHearing other) {
        if (slotDate == null) {
            if (other.slotDate != null)
                return false;
        } else if (!(slotDate.getTime() == other.slotDate.getTime()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RoomDateForHearing [roomName=" + roomName + ", slotDate=" + slotDate + ", hearingCount=" + hearingCount + "]";
    }

    @Override
    public int compareTo(RoomDateForHearing o) {
        if (this.getHearingCount() < o.getHearingCount()) {
            return -1;
        }
        return 1;
    }

}
