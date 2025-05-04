import React, { useState } from 'react';
import { deleteSeat, updateSeat, addSeatToTrain } from '../services/api';

function SeatTable({ seats, trainId, onUpdate }) {
    const [editingSeat, setEditingSeat] = useState(null);
    const [showAddForm, setShowAddForm] = useState(false);
    const [newSeat, setNewSeat] = useState({
        number: '',
        type: 'Эконом',
        price: '',
        isFree: true
    });

    const handleDelete = async (seatId) => {
        try {
            await deleteSeat(seatId);
            onUpdate();
        } catch (error) {
            console.error('Error deleting seat:', error);
        }
    };

    const handleUpdate = async (updatedSeat) => {
        try {
            await updateSeat(updatedSeat.id, updatedSeat);
            setEditingSeat(null);
            onUpdate();
        } catch (error) {
            console.error('Error updating seat:', error);
        }
    };

    const handleAddSeat = async (e) => {
        e.preventDefault();
        try {
            await addSeatToTrain(trainId, {
                ...newSeat,
                number: Number(newSeat.number),
                price: Number(newSeat.price)
            });
            setShowAddForm(false);
            setNewSeat({
                number: '',
                type: 'Эконом',
                price: '',
                isFree: true
            });
            onUpdate();
        } catch (error) {
            console.error('Error adding seat:', error);
        }
    };

    const sortedSeats = [...seats].sort((a, b) => a.number - b.number);

    return (
        <div className="seats-container">
            <div className="seats-grid">
                {seats.length > 0 ? (
                    sortedSeats.map((seat) => (
                        <div
                            key={seat.id}
                            className={`seat-card ${seat.isFree ? 'free' : 'occupied'}`}
                        >
                            {editingSeat?.id === seat.id ? (
                                <div className="seat-edit-form">
                                    <input
                                        type="number"
                                        value={editingSeat.number}
                                        onChange={(e) => setEditingSeat({
                                            ...editingSeat,
                                            number: e.target.value
                                        })}
                                        min="1"
                                        disabled
                                    />
                                    <select
                                        value={editingSeat.type}
                                        onChange={(e) => setEditingSeat({
                                            ...editingSeat,
                                            type: e.target.value
                                        })}
                                    >
                                        <option value="Эконом">Эконом</option>
                                        <option value="Бизнес">Бизнес</option>
                                        <option value="Плацкартный">Плацкартный</option>
                                        <option value="Купейный">Купейный</option>
                                        <option value="СВ">СВ</option>
                                        <option value="Другое">Другое</option>
                                    </select>
                                    <input
                                        type="number"
                                        step="0.01"
                                        value={editingSeat.price}
                                        onChange={(e) => setEditingSeat({
                                            ...editingSeat,
                                            price: e.target.value
                                        })}
                                        min="0"
                                        required
                                    />
                                    <label>
                                        <input
                                            type="checkbox"
                                            checked={editingSeat.isFree}
                                            onChange={(e) => setEditingSeat({
                                                ...editingSeat,
                                                isFree: e.target.checked
                                            })}
                                        />
                                        Свободно?
                                    </label>
                                    <div className="seat-actions">
                                        <button onClick={() => handleUpdate(editingSeat)}>Сохранить</button>
                                        <button onClick={() => setEditingSeat(null)}>Назад</button>
                                    </div>
                                </div>
                            ) : (
                                <>
                                    <div className="seat-number">{seat.number}</div>
                                    <div className="seat-type">{seat.type}</div>
                                    <div className="seat-price">{seat.price.toFixed(2)} ₽</div>
                                    <div className="seat-status">
                                        {seat.isFree ? 'Свободно' : 'Занято'}
                                    </div>
                                    <div className="seat-hover-actions">
                                        <button
                                            onClick={() => setEditingSeat(seat)}
                                            className="edit-btn"
                                        >
                                            Изменить
                                        </button>
                                        <button
                                            onClick={() => handleDelete(seat.id)}
                                            className="delete-btn"
                                        >
                                            Удалить
                                        </button>
                                    </div>
                                </>
                            )}
                        </div>
                    ))
                ) : (
                    <div className="no-seats-message">
                        <p>Нет никаких мест, добавьте первое место</p>
                        <div className="add-seat-section">
                            <button
                                onClick={() => setShowAddForm(!showAddForm)}
                                className={`toggle-add-form-btn ${showAddForm ? 'cancel' : 'add'}`}
                            >
                                {showAddForm ? 'Назад' : 'Добавить новое место'}
                            </button>
                        </div>
                    </div>
                )}
            </div>
            {seats.length > 0 && (
                <div className="add-seat-section">
                    <button
                        onClick={() => setShowAddForm(!showAddForm)}
                        className={`toggle-add-form-btn ${showAddForm ? 'cancel' : 'add'}`}
                    >
                        {showAddForm ? 'Назад' : 'Добавить новое место'}
                    </button>
                </div>
            )}

            {showAddForm && (
                <form onSubmit={handleAddSeat} className="add-seat-form">
                    <input
                        type="number"
                        placeholder="Номер"
                        value={newSeat.number}
                        onChange={(e) => setNewSeat({
                            ...newSeat,
                            number: e.target.value
                        })}
                        required
                        min="1"
                    />
                    <select
                        value={newSeat.type}
                        onChange={(e) => setNewSeat({
                            ...newSeat,
                            type: e.target.value
                        })}
                    >
                        <option value="Эконом">Эконом</option>
                        <option value="Бизнес">Бизнес</option>
                        <option value="Плацкартный">Плацкартный</option>
                        <option value="Купейный">Купейный</option>
                        <option value="СВ">СВ</option>
                        <option value="Другое">Другое</option>
                    </select>
                    <input
                        type="number"
                        step="0.01"
                        placeholder="Стоимость"
                        value={newSeat.price}
                        onChange={(e) => setNewSeat({
                            ...newSeat,
                            price: e.target.value
                        })}
                        required
                        min="0"
                    />
                    <label>
                        <input
                            type="checkbox"
                            checked={newSeat.isFree}
                            onChange={(e) => setNewSeat({
                                ...newSeat,
                                isFree: e.target.checked
                            })}
                        />
                        Свободно?
                    </label>
                    <button type="submit">Добавить место</button>
                    <button
                        type="button"
                        onClick={() => setShowAddForm(false)}
                    >
                        Назад
                    </button>
                </form>
            )}
        </div>
    );
}

export default SeatTable;