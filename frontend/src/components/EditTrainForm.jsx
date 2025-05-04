import React, { useState } from 'react';
import { updateTrain } from '../services/api';

function EditTrainForm({ train, onSuccess, onCancel }) {
    const [trainData, setTrainData] = useState({
        number: train.number,
        departureStation: train.departureStation,
        arrivalStation: train.arrivalStation,
        departureTime: train.departureTime,
        arrivalTime: train.arrivalTime
    });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await updateTrain(train.number, trainData);
            onSuccess();
        } catch (error) {
            console.error('Error updating train:', error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setTrainData(prev => ({ ...prev, [name]: value }));
    };

    return (
        <div className="edit-form-overlay">
            <form onSubmit={handleSubmit} className="edit-train-form">
                <div className="form-header">
                    <h3>Изменить информацию</h3>
                </div>

                <div className="form-grid">
                    <div className="form-group full-width">
                        <label>Номер поезда</label>
                        <input
                            name="number"
                            value={trainData.number}
                            onChange={handleChange}
                            disabled
                            className="disabled-input"
                        />
                    </div>

                    <div className="form-group">
                        <label>Станция отправления</label>
                        <input
                            name="departureStation"
                            value={trainData.departureStation}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Время отправления (HH:MM)</label>
                        <input
                            name="departureTime"
                            value={trainData.departureTime}
                            onChange={handleChange}
                            required
                            pattern="^([01]?[0-9]|2[0-3]):[0-5][0-9]$"
                        />
                    </div>

                    <div className="form-group">
                        <label>Станция прибытия</label>
                        <input
                            name="arrivalStation"
                            value={trainData.arrivalStation}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Время прибытия (HH:MM)</label>
                        <input
                            name="arrivalTime"
                            value={trainData.arrivalTime}
                            onChange={handleChange}
                            required
                            pattern="^([01]?[0-9]|2[0-3]):[0-5][0-9]$"
                        />
                    </div>
                </div>

                <div className="form-actions">
                    <button type="submit" className="save-btn">Сохранить изменения</button>
                    <button type="button" onClick={onCancel} className="cancel-btn">Отмена</button>
                </div>
            </form>
        </div>
    );
}

export default EditTrainForm;