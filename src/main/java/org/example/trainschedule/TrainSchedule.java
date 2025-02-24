package org.example.trainschedule;

@SuppressWarnings("checkstyle:LineLength")
public class TrainSchedule {
    private String trainId;
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
    private String arrivalTime;

    public TrainSchedule() {
        // TODO:
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
        // TODO:setting train ID
        this.trainId = trainId;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        // TODO:setting departure station
        this.departureStation = departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        // TODO: setting arrival station
        this.arrivalStation = arrivalStation;
    }

    public String getDepartureTime() {
        // TODO: we will get departure time
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        // TODO: setting departure time
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        // TODO: get arrival time
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        // TODO: set arrival time
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
