import React, { useState } from 'react';
import { addSeatToTrain } from '../services/api';

function AddSeatForm({ trainId, onSuccess, onCancel }) {
    const [seatData, setSeatData] = useState({
        number: '',
        type: 'Эконом',
        price: '',
        isFree: true
    });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await addSeatToTrain(trainId, {
                ...seatData,
                number: Number(seatData.number),
                price: Number(seatData.price)
            });
            onSuccess();
        } catch (error) {
            console.error('Error adding seat:', error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setSeatData(prev => ({
            ...prev,
            [name]: name === 'isFree' ? e.target.checked : value
        }));
    };

    return (
        <form onSubmit={handleSubmit} className="add-seat-form">
            <input
                name="number"
                type="number"
                placeholder="Seat Number"
                value={seatData.number}
                onChange={handleChange}
                required
                min="1"
            />

            <select
                name="type"
                value={seatData.type}
                onChange={handleChange}
                required
            >
                <option value="Эконом">Эконом</option>
                <option value="Бизнес">Бизнес</option>
                <option value="Плацкартный">Плацкартный</option>
                <option value="Купейный">Купейный</option>
                <option value="СВ">СВ</option>
                <option value="Другое">Другое</option>
            </select>

            <input
                name="price"
                type="number"
                step="0.01"
                placeholder="Price"
                value={seatData.price}
                onChange={handleChange}
                required
                min="0"
            />

            <label>
                <input
                    name="isFree"
                    type="checkbox"
                    checked={seatData.isFree}
                    onChange={handleChange}
                />
                Available
            </label>

            <div className="form-actions">
                <button type="submit">Add Seat</button>
                <button type="button" onClick={onCancel}>Cancel</button>
            </div>
        </form>
    );
}

export default AddSeatForm;