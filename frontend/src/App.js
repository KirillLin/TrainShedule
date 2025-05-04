import React, { useState, useEffect } from 'react';
import './App.css';
import SearchForm from './components/SearchForm';
import TrainTable from './components/TrainTable';
import { getAllTrains, searchTrains, searchTrainByNumber} from './services/api';

function App() {
    const [trains, setTrains] = useState([]);
    const [selectedTrain, setSelectedTrain] = useState(null);

    const fetchAllTrains = async () => {
        try {
            const data = await getAllTrains();
            setTrains(data);
        } catch (error) {
            console.error('Error fetching trains:', error);
        }
    };

    const handleNumberSearch = async (number) => {
        try {
            const data = await searchTrainByNumber(number);
            setTrains(data);
        } catch (error) {
            console.error('Error searching trains:', error);
        }
    };

    const handleRouteSearch = async (departure, arrival) => {
        try {
            const data = await searchTrains(departure, arrival);
            setTrains(data);
        } catch (error) {
            console.error('Error searching trains:', error);
        }
    };

    useEffect(() => {
        fetchAllTrains();
    }, []);

    return (
        <div className="app">
            <div className="header-section">
                <h1>Менеджер поездов</h1>
                <SearchForm
                    onSearch={handleRouteSearch}
                    onShowAll={fetchAllTrains}
                    onNumberSearch={handleNumberSearch}
                />
            </div>
            <TrainTable
                trains={trains}
                onSelectTrain={setSelectedTrain}
                onUpdate={fetchAllTrains}
            />
        </div>
    );
}

export default App;
