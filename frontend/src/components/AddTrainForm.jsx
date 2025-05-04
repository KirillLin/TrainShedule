import React, { useState } from 'react';
import { createTrain } from '../services/api';

function AddTrainForm({ onSuccess }) {
    const [trainData, setTrainData] = useState({
        number: '',
        departureStation: '',
        arrivalStation: '',
        departureTime: '',
        arrivalTime: ''
    });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await createTrain(trainData);
            onSuccess();
        } catch (error) {
            console.error('Error creating train:', error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setTrainData(prev => ({ ...prev, [name]: value }));
    };

    return (
        <form onSubmit={handleSubmit} className="add-train-form">
            <input
                name="number"
                placeholder="Номер"
                value={trainData.number}
                onChange={handleChange}
                required
            />
            <input
                name="departureStation"
                placeholder="Станция отправления"
                value={trainData.departureStation}
                onChange={handleChange}
                required
            />
            <input
                name="arrivalStation"
                placeholder="Станция прибытия"
                value={trainData.arrivalStation}
                onChange={handleChange}
                required
            />
            <input
                name="departureTime"
                placeholder="Время отправления (HH:MM)"
                value={trainData.departureTime}
                onChange={handleChange}
                required
                pattern="^([01]?[0-9]|2[0-3]):[0-5][0-9]$"
            />
            <input
                name="arrivalTime"
                placeholder="Время прибытия (HH:MM)"
                value={trainData.arrivalTime}
                onChange={handleChange}
                required
                pattern="^([01]?[0-9]|2[0-3]):[0-5][0-9]$"
            />
            <button type="submit">Добавить</button>
        </form>
    );
}

export default AddTrainForm;