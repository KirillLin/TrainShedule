package org.example.trainschedule;

@SuppressWarnings("checkstyle:LineLength")
public class TrainSchedule {
    private String trainId;
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
    private String arrivalTime;

    public TrainSchedule() {
        this.departureStation = "Default Departure";
        this.arrivalStation = "Default Arrival";
    }

    public TrainSchedule(String trainId, String departureStation, String arrivalStation, String departureTime, String arrivalTime) {
        this.trainId = trainId;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "TrainSchedule{" +
                "trainId='" + trainId + '\'' +
                ", departureStation='" + departureStation + '\'' +
                ", arrivalStation='" + arrivalStation + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                '}';
    }
}
