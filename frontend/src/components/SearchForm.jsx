import React, { useState } from 'react';

function SearchForm({ onSearch, onShowAll, onNumberSearch }) {
    const [departure, setDeparture] = useState('');
    const [arrival, setArrival] = useState('');
    const [trainNumber, setTrainNumber] = useState('');

    const handleRouteSubmit = (e) => {
        e.preventDefault();
        onSearch(departure, arrival);
    };

    const handleNumberSubmit = (e) => {
        e.preventDefault();
        onNumberSearch(trainNumber);
    };

    return (
        <div className="search-form">
            <div className="search-grid">
                <div className="search-column">
                    <h3>Найти маршрут с свободными местами</h3>
                    <form onSubmit={handleRouteSubmit} className="route-form">
                        <div className="form-row">
                            <label>От:</label>
                            <input
                                type="text"
                                placeholder="Откуда"
                                value={departure}
                                onChange={(e) => setDeparture(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-row">
                            <label>До:</label>
                            <input
                                type="text"
                                placeholder="Куда"
                                value={arrival}
                                onChange={(e) => setArrival(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit">Найти</button>
                    </form>
                </div>

                <div className="search-column">
                    <h3>Дополнительно</h3>
                    <div className="search-options">
                        <div className="search-row">
                            <form onSubmit={handleNumberSubmit} className="number-form">
                                <div className="input-group">
                                    <input
                                        type="text"
                                        placeholder="Номер поезда"
                                        value={trainNumber}
                                        onChange={(e) => setTrainNumber(e.target.value)}
                                        required
                                    />
                                </div>
                            </form>
                        </div>
                        <button type="submit" className="search-btn">
                            Найти поезд по номеру
                        </button>
                        <div className="search-row">
                            <button onClick={onShowAll} className="show-all-btn">
                                Все поезда
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default SearchForm;