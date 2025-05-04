import React, { useState } from 'react';
import SeatTable from './SeatTable';
import AddTrainForm from './AddTrainForm';
import EditTrainForm from './EditTrainForm';
import AddSeatForm from './AddSeatForm';
import { deleteTrain } from '../services/api';

function TrainTable({ trains, onSelectTrain, onUpdate }) {
    const [addingTrain, setAddingTrain] = useState(false);
    const [editingTrain, setEditingTrain] = useState(null);
    const [addingSeat, setAddingSeat] = useState(null);
    const [expandedTrains, setExpandedTrains] = useState({});

    const toggleExpandTrain = (trainId) => {
        setExpandedTrains(prev => ({
            ...prev,
            [trainId]: !prev[trainId]
        }));
    };

    const handleDelete = async (trainId) => {
        try {
            await deleteTrain(trainId);
            onUpdate();
        } catch (error) {
            console.error('Error deleting train:', error);
        }
    };

    const sortedTrains = [...trains].sort((a, b) => a.number - b.number);

    return (
        <div className="train-table">
            <table>
                <thead>
                <tr>
                    <th>Номер</th>
                    <th>Станция отправления</th>
                    <th>Время</th>
                    <th>Станция прибытия</th>
                    <th>Время</th>
                    <th>Действия</th>
                </tr>
                </thead>
                <tbody>
                {sortedTrains.map((train) => (
                    <React.Fragment key={train.id}>
                        <tr>
                            <td>{train.number}</td>
                            <td>{train.departureStation}</td>
                            <td>{train.departureTime}</td>
                            <td>{train.arrivalStation}</td>
                            <td>{train.arrivalTime}</td>
                            <td className="actions-column">
                                <button
                                    onClick={() => toggleExpandTrain(train.id)}
                                    className={`seat-toggle-btn ${expandedTrains[train.id] ? 'active' : ''}`}
                                >
                                    {expandedTrains[train.id] ? 'Скрыть' : 'Показать места'}
                                </button>
                                <button
                                    onClick={() => setEditingTrain(train)}
                                    className="edit-btn"
                                >
                                    Изменить
                                </button>
                                <button
                                    onClick={() => handleDelete(train.id)}
                                    className="delete-btn"
                                >
                                    Удалить
                                </button>
                            </td>
                        </tr>

                        {expandedTrains[train.id] && (
                            <tr className="expanded-row">
                                <td colSpan="6">
                                    <div className="seats-section">
                                        {addingSeat === train.id && (
                                            <AddSeatForm
                                                trainId={train.id}
                                                onSuccess={() => {
                                                    onUpdate();
                                                    setAddingSeat(null);
                                                }}
                                                onCancel={() => setAddingSeat(null)}
                                            />
                                        )}

                                        {train.seats && train.seats.length >= 0 ? (
                                            <SeatTable
                                                seats={train.seats}
                                                trainId={train.id}
                                                onUpdate={onUpdate}
                                            />
                                        ) : (
                                            <p className="no-seats">Нет никаких мест</p>
                                        )}
                                    </div>
                                </td>
                            </tr>
                        )}
                    </React.Fragment>
                ))}
                </tbody>
            </table>

            <button
                onClick={() => setAddingTrain(!addingTrain)}
                className={`add-train-btn ${addingTrain ? 'cancel' : ''}`}
            >
                {addingTrain ? 'Отмена' : 'Добавить поезд'}
            </button>

            {addingTrain && (
                <AddTrainForm
                    onSuccess={() => {
                        onUpdate();
                        setAddingTrain(false);
                    }}
                    onCancel={() => setAddingTrain(false)}
                />
            )}

            {editingTrain && (
                <EditTrainForm
                    train={editingTrain}
                    onSuccess={() => {
                        onUpdate();
                        setEditingTrain(null);
                    }}
                    onCancel={() => setEditingTrain(null)}
                />
            )}
        </div>
    );
}

export default TrainTable;