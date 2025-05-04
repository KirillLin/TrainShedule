import axios from 'axios';
const API_BASE = 'http://localhost:8080/api';

export const getAllTrains = async () => {
    const response = await fetch(`${API_BASE}/trains`);
    return response.json();
};

export const searchTrains = async (departure, arrival) => {
    const response = await fetch(
        `${API_BASE}/trains/with-free-seats?departure=${encodeURIComponent(departure)}&arrival=${encodeURIComponent(arrival)}`
    );
    return response.json();
};

export const createTrain = async (trainData) => {
    const response = await fetch(`${API_BASE}/trains`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(trainData),
    });
    return response.json();
};

export const deleteTrain = async (trainId) => {
    await fetch(`${API_BASE}/trains/${trainId}`, {
        method: 'DELETE',
    });
};

export const deleteSeat = async (seatId) => {
    await fetch(`${API_BASE}/seats/${seatId}`, {
        method: 'DELETE',
    });
};

export const updateSeat = async (seatId, seatData) => {
    const response = await fetch(`${API_BASE}/seats/${seatId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(seatData),
    });
    return response.json();
};

export const addSeatToTrain = async (trainId, seatData) => {
    const response = await fetch(`${API_BASE}/seats/${trainId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(seatData),
    });
    return response.json();
};

export const updateTrain = async (number, trainData) => {
    const response = await fetch(`${API_BASE}/trains/by-number/${number}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(trainData),
    });
    return response.json();
};

export const getSeatsByTrainId = async (trainId) => {
    const response = await axios.get(`${API_BASE}/seats/train/${trainId}`);
    return response.data;
};

export const searchTrainByNumber = async (trainNumber) => {
    const response = await axios.get(`${API_BASE}/trains/search?number=${trainNumber}`);
    return response.data;
};