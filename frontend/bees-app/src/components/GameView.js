import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../App.css';
import HiveCard from './HiveCard';
import { getGame, iterateWeek, submitActionsOfTheWeek } from './BeesApiService';
import rapeseedFlower from '../rapeseed-flower.jpg';
import wildFlower from '../wild-flower.jpg';
import acaciaFlower from '../acacia-flower.jpg';
import lindenFlower from '../linden-flower.jpg';
import sunFlower from '../sun-flower.jpg';
import falseIndigoFlower from '../false-indigo-flower.jpg';




const GameView = () => {
    const navigate = useNavigate();
    const [gameData, setGameData] = useState(null);
    const [selectedActions, setSelectedActions] = useState({});
    const [updatedGameData, setUpdatedGameData] = useState(null);

    useEffect(() => {
        async function fetchGameData() {
            try {
                const data = await getGame();
                console.log('Game data:', data);
                setGameData(data);
                setUpdatedGameData(data);
            } catch (error) {
                console.error('Error fetching game data:', error);
            }
        }
        fetchGameData();
    }, []);

    const handleCheckboxChange = (actionMarker, hiveId) => {
        setSelectedActions((prevSelectedActions) => ({
            ...prevSelectedActions,
            [`${actionMarker}-${hiveId}`]: !prevSelectedActions[`${actionMarker}-${hiveId}`],
        }));
    };


    const handleSubmit = async () => {
        const filteredActions = updatedGameData.actionOfTheWeek.map((actionItem) => {
            const selectedHiveIds = actionItem.hiveIds.filter((hiveId) => selectedActions[`${actionItem.actionOfTheWeekMarker}-${hiveId}`]);
            return selectedHiveIds.length > 0
                ? {
                    actionOfTheWeekMessage: actionItem.actionOfTheWeekMessage,
                    actionOfTheWeekMarker: actionItem.actionOfTheWeekMarker,
                    hiveIds: selectedHiveIds,
                }
                : null;
        }).filter(action => action !== null);

        if (filteredActions.length > 0) {
            try {
                const response = await submitActionsOfTheWeek(filteredActions);
                console.log('Data sent successfully:', response);

                const updatedData = await getGame();
                console.log('Updated game data after submit:', updatedData);

                setGameData(updatedData);
                setUpdatedGameData(updatedData);

                setUpdatedGameData((prevData) => ({
                    ...prevData,
                    actionOfTheWeek: []
                }));
            } catch (error) {
                console.error('Error submitting data:', error);
            }
        } else {
            console.log('No actions selected');
        }
    };




    const handleIterateWeek = async () => {
        try {
            const updatedGameData = await iterateWeek();
            console.log('Updated game data:', updatedGameData);
            setGameData(updatedGameData);
            setUpdatedGameData(updatedGameData);
            setSelectedActions({});
        } catch (error) {
            console.error('Error iterating week:', error);
        }
    };
    

    const getFlowerImage = () => {
        if (!gameData || !gameData.currentDate) {
            return wildFlower;
        }

        const currentDate = new Date(gameData.currentDate);
        const month = currentDate.getMonth() + 1;
        const day = currentDate.getDate();
        if (month === 3 || month === 8 || month === 9) {
            return wildFlower;
        } else if (month === 4 && day <= 20) {
            return rapeseedFlower;
        } else if (month === 4 && day >= 21) {
            return wildFlower;
        } else if (month === 5 && day <= 20) {
            return acaciaFlower;
        } else if (month === 5 && day >= 21 && day <= 31) {
            return falseIndigoFlower;
        } else if (month === 6 && day <= 20) {
            return lindenFlower;
        } else if (month === 6 && day >= 21) {
            return wildFlower;
        } else if (month === 7) {
            return sunFlower;
        } else {
            return wildFlower;
        }
    };

    return (
        <div className="body-gameView">
            <div className="row">
                <div className="col-md-6">
                    <div className="row">
                        {gameData && gameData.hives && gameData.hives.length > 0 ? (
                            gameData.hives.map((hive, index) => (
                                <div className="col-md-6 mb-3" key={hive.id}>
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
                            {updatedGameData && Array.isArray(updatedGameData.actionOfTheWeek) && updatedGameData.actionOfTheWeek.length > 0 ? (
                                <div>
                                    <p>Action of the week:</p>
                                    <form>
                                        {updatedGameData.actionOfTheWeek.map((actionItem, actionIndex) => (
                                            <div key={actionIndex}>
                                                <p>{actionItem.actionOfTheWeekMessage}:</p>
                                                {actionItem.hiveIds.map((hiveId, hiveIndex) => (
                                                    <div key={`${actionIndex}-${hiveIndex}`} className="form-check">
                                                        <input
                                                            type="checkbox"
                                                            className="form-check-input"
                                                            id={`hive-${actionItem.actionOfTheWeekMarker}-${hiveId}`}
                                                            checked={selectedActions[`${actionItem.actionOfTheWeekMarker}-${hiveId}`] || false}
                                                            onChange={() => handleCheckboxChange(actionItem.actionOfTheWeekMarker, hiveId)}
                                                        />
                                                        <label className="form-check-label" htmlFor={`hive-${actionItem.actionOfTheWeekMarker}-${hiveId}`}>
                                                            Hive {hiveId}
                                                        </label>
                                                    </div>
                                                ))}
                                            </div>
                                        ))}
                                    </form>
                                    <button className="btn btn-success mt-3" onClick={handleSubmit}>Submit</button>
                                </div>
                            ) : (
                                <p>Weekly checking</p>
                            )}
                        </div>

                    </div>
                </div>
                <div className="col-md-3">
                    <div className="d-flex flex-column">
                        <p className="btn-custom p-custom mb-2">Date: {gameData ? gameData.currentDate : 'Loading...'}</p>
                        <p className="btn-custom p-custom mb-2">Temp: {gameData && gameData.temperature ? gameData.temperature.toFixed(2) : 'Loading...'}</p>
                        <p className="btn-custom p-custom mb-2">Wind speed: {gameData && gameData.windSpeed ? gameData.windSpeed.toFixed(2) : 'Loading...'}</p>
                        <p className="btn-custom p-custom mb-2">Precipitation: {gameData && gameData.precipitation ? gameData.precipitation.toFixed(2) : 'Loading...'}</p>
                        <p className="btn-custom p-custom mb-2">Total honey harvested: {gameData ? (gameData.totalKgOfHoney ?? 'Loading...').toFixed(2) : 'Loading...'}</p>
                        <p className="btn-custom p-custom mb-2">Money in the bank: {gameData && gameData.moneyInTheBank ? gameData.moneyInTheBank.toFixed(2) : 'Loading...'}</p>
                        <img src={getFlowerImage()} alt="Flower based on date" className="img-custom mb-2" />
                        <button className="btn btn-custom p-custom mb-2" onClick={handleIterateWeek}>Iterate one week</button>
                        <button className="btn btn-custom p-custom mb-2" onClick={() => navigate('/sell-honey')}>Sell Honey</button>
                        <button className="btn btn-custom mb-2">Buy Hives</button>
                        <button className="btn btn-danger btn-custom mb-2" onClick={() => navigate('/')}>Exit</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default GameView;
