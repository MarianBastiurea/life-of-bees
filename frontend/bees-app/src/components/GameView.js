import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../App.css';
import HiveCard from './HiveCard';
import { getGame } from './BeesApiService';
import rapeseedFlower from '../rapeseed-flower.jpg';

const GameView = () => {
    const navigate = useNavigate();
    const [gameData, setGameData] = useState(null);

    useEffect(() => {
        async function fetchGameData() {
            try {
                const data = await getGame();
                console.log('Game data:', data);
                setGameData(data);
            } catch (error) {
                console.error('Error fetching game data:', error);
            }
        }
        fetchGameData();
    }, []);

    const handleAnswer = (answer) => {
        console.log(`User answered: ${answer}`);
    };

    const handleIterateWeek = async () => {
        try {
            const updatedGameData = await iterateWeek(); // Apelează backend-ul
            console.log('Updated game data:', updatedGameData);
            setGameData(updatedGameData); // Actualizează datele jocului
        } catch (error) {
            console.error('Error iterating week:', error);
        }
    };


    return (
        <div className="body-gameView">
            <div className="row">
                <div className="col-md-6">
                    <div className="row">
                        {gameData && gameData.length > 0 ? (
                            gameData.map((hive, index) => (
                                <div className="col-md-6 mb-3" key={hive.hiveId}>
                                    <HiveCard hive={hive} />
                                </div>
                            ))
                        ) : (
                            <p>No hives available or data not loaded yet.</p>
                        )}
                    </div>
                </div>
                <div className="col-md-3">
                    <div className="card mb-3">
                        <div className="card-body">
                            <p>Action of the week:</p>
                            <p>Insect control</p>
                            <p>Yes or No?</p>
                            <div>
                                <button className="btn btn-success me-2" onClick={() => handleAnswer("Yes")}>Yes</button>
                                <button className="btn btn-danger" onClick={() => handleAnswer("No")}>No</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="col-md-3">
                    <div className="d-flex flex-column">
                        <p className="btn-custom p-custom mb-2">Date: {gameData ? gameData[0].currentDate : 'Loading...'}</p>
                        <p className="btn-custom p-custom mb-2">Temp: {gameData ? gameData[0].temperature.toFixed(2) : 'Loading...'}</p>
                        <p className="btn-custom p-custom mb-2">Wind speed: {gameData ? gameData[0].speedWind.toFixed(2) : 'Loading...'}</p>
                        <p className="btn-custom p-custom mb-2">Total honey: {gameData ? gameData.reduce((sum, hive) => sum + hive.kgOfHoney, 0).toFixed(2) : 'Loading...'}</p>
                        <img src={rapeseedFlower} alt="Imagine Buton 5" className="img-custom mb-2" />
                        <button className="btn btn-custom p-custom mb-2" onClick={() => navigate('/sell-honey')}>Sell Honey</button>
                        <button className="btn btn-custom mb-2">Buy Hives</button>
                        <button className="btn btn-custom mb-2" onClick={handleIterateWeek}>Iterate next week</button>
                        <button className="btn btn-danger btn-custom mb-2" onClick={() => navigate('/')}>Exit</button>
                    </div>
                </div>
            </div>
        </div >
    );
};

export default GameView;
