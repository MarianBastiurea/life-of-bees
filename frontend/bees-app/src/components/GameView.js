import React, { useState, useEffect, useMemo } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router-dom';
import '../App.css';
import HiveCard from './HiveCard';
import { getGame, iterateWeek, submitActionsOfTheWeek, buyHives } from './BeesApiService';
import rapeseedFlower from '../rapeseed-flower.jpg';
import wildFlower from '../wild-flower.jpg';
import acaciaFlower from '../acacia-flower.jpg';
import lindenFlower from '../linden-flower.jpg';
import sunFlower from '../sun-flower.jpg';
import falseIndigoFlower from '../false-indigo-flower.jpg';
import BuyHivesModal from './BuyHivesModal';


const GameView = () => {
    const navigate = useNavigate();
    const [gameData, setGameData] = useState(null);
    const [selectedActions, setSelectedActions] = useState({});
    const [updatedGameData, setUpdatedGameData] = useState(null);
    const locationData = useLocation();

    const { gameId: gameIdFromParams } = useParams(); 
    const { gameId: gameIdFromState } = locationData.state || {};
    const gameId = gameIdFromParams || gameIdFromState;

    const [loading, setLoading] = useState(false);
    const [month, setMonth] = useState(null);
    const [day, setDay] = useState(null);


    useEffect(() => {
        if (!gameId) {
            console.error('Game ID is missing!');
            return;
        }

        async function fetchGameData() {
            try {
                console.log('am primit datele in gameView pentru ID:', gameId);
                const data = await getGame(gameId);
                console.log('datele primite din Java:', data);
                const currentDate = new Date(data.currentDate); 
                setMonth(currentDate.getMonth() + 1); 
                console.log('luna curenta este: ', currentDate.getMonth() + 1);
                setDay(currentDate.getDate()); 
                console.log('ziua este: '+currentDate.getDate());
                setGameData(data);
                setUpdatedGameData(data);


            } catch (error) {
                console.error('No receiving data in GameView:', error);
            } finally {
                setLoading(false);
            }
        }

        fetchGameData();
    }, []);



    const handleCheckboxChange = (actionType, hiveId) => {
        setSelectedActions((prevSelectedActions) => ({
            ...prevSelectedActions,
            [`${actionType}-${hiveId}`]: !prevSelectedActions[`${actionType}-${hiveId}`],
        }));
    };

    const handleSubmit = async () => {
        try {
            const actionsByType = Object.keys(selectedActions)
                .filter(key => selectedActions[key])
                .reduce((acc, key) => {
                    const [actionType, sourceHiveId, destinationHiveId] = key.split('-');
                    if (!acc[actionType]) {
                        acc[actionType] = {
                            actionType,
                            data: {}
                        };
                    }

                    if (['ADD_EGGS_FRAME', 'ADD_HONEY_FRAME', 'SPLIT_HIVE'].includes(actionType)) {
                        if (!acc[actionType].data.hiveIds) acc[actionType].data.hiveIds = [];
                        acc[actionType].data.hiveIds.push(parseInt(sourceHiveId));
                    }

                    else if (actionType === 'MOVE_EGGS_FRAME' && sourceHiveId && destinationHiveId) {
                        if (!acc[actionType].data.hiveIdPairs) acc[actionType].data.hiveIdPairs = [];
                        acc[actionType].data.hiveIdPairs.push([parseInt(sourceHiveId), parseInt(destinationHiveId)]);
                    }

                    else {
                        acc[actionType].data.answer = selectedActions[key];
                    }

                    return acc;
                }, {});

            const aggregatedActions = Object.values(actionsByType).map(action => {
                if (action.data.hiveIds) {
                    action.data.hiveIds = [...new Set(action.data.hiveIds)];
                }
                if (action.data.hiveIdPairs) {
                    action.data.hiveIdPairs = [...new Set(action.data.hiveIdPairs.map(JSON.stringify))].map(JSON.parse);
                }
                return action;
            });

            console.log("ActionOfTheWeek data being sent:", aggregatedActions);
            const response = await submitActionsOfTheWeek(gameId, aggregatedActions);
            console.log("Response from backend:", response);

            if (response) {
                console.log("Actions submitted successfully!");
                const updatedData = await getGame(gameId);
                setGameData(updatedData);
                setUpdatedGameData(updatedData);
                setSelectedActions({});
            }
        } catch (error) {
            console.error("Error submitting actions:", error);
        }
    };

    const handleYesNoChange = (actionType, response) => {
        setSelectedActions((prevSelectedActions) => ({
            ...prevSelectedActions,
            [actionType]: response,
        }));
    };

    const handleIterateWeek = async () => {
        if (!gameId) {
            console.error("Game ID is missing!");
            return;
        }
    
        try {
            const updatedGameData = await iterateWeek(gameId); 
            console.log('Datele din iterateOneWeek:', updatedGameData);
    
            setGameData(updatedGameData); 
            setUpdatedGameData(updatedGameData); 
            setSelectedActions({}); 

            const currentDate = new Date(updatedGameData.currentDate);
            setMonth(currentDate.getMonth() + 1); 
            setDay(currentDate.getDate()); 
        } catch (error) {
            console.error('Error iterating week:', error);
        }
    };
    


    const flowerImage = useMemo(() => {
        if (!month || !day) {
            return wildFlower; 
        }
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
    }, [month, day]); 


    const formatActionType = (actionType) => {
        return actionType
            .toLowerCase()
            .replace(/_/g, ' ')
            .replace(/\b\w/g, char => char.toUpperCase());
    };

    const [showBuyHivesForm, setShowBuyHivesForm] = useState(false);
    const [hivesToBuy, setHivesToBuy] = useState(1);
    const [error, setError] = useState(null);
    const maxHives = gameData ? 10 - gameData.hives.length : 0;




    const handleBuyHivesClick = () => {
        if (month === 3 || month === 4) {
            setShowBuyHivesForm(true);
            setError(null);
        } else {
            setError("You can only buy hives in March or April.");
            setShowBuyHivesForm(false);
        }
    };

    const handleHivesToBuyChange = (e) => {
        const value = Math.min(e.target.value, maxHives);
        if (value * 500 <= gameData.moneyInTheBank) {
            setHivesToBuy(value);
            setError(null);
        } else {
            setError("Insufficient funds to buy that many hives.");
        }
    };



    const handleSubmitHivesPurchase = async () => {
        if (hivesToBuy > maxHives) {
            setError(`You can only buy up to ${maxHives} hives.`);
            return;
        }
        if (gameData.moneyInTheBank < hivesToBuy * 500) {
            setError("Insufficient funds to buy hives.");
            return;
        }

        try {
            const gameId = gameData.id; 
            const response = await buyHives(gameId, hivesToBuy); 
            if (response) {
                setShowBuyHivesForm(false);
                setError(null);
                setGameData(await getGame(gameId)); 
            } else {
                setError('Failed to buy hives.');
            }
        } catch (error) {
            setError('An error occurred.');
        }
    };

    const isMarchOrApril = () => {
        if (!gameData || !gameData.currentDate) {
            return false;
        }
        const currentMonth = new Date(gameData.currentDate).getMonth() + 1;
        return currentMonth === 3 || currentMonth === 4;
    };

    const goToHiveHistory = (hiveId) => {
        if (!gameId) {
            console.error("gameId is missing");
            return;
        }
        console.log("Navigating to HiveHistory with gameId:", gameId, "and hiveId:", hiveId);
        navigate('/HiveHistory', { state: { gameId: gameId, hiveId: hiveId } });
    };
    
    





    return (
        <div className="body-gameView">

            <div className="row">
                <div className="col-md-6">
                    <div className="row">
                        {gameData && gameData.hives && gameData.hives.length > 0 ? (
                            gameData.hives.map((hive, index) => (
                                <button     
                                    key={hive.id} 
                                    className="col-md-6 mb-3 btn btn-outline-primary"
                                    onClick={() => goToHiveHistory(hive.id)}
                                    style={{ cursor: 'pointer', textAlign: 'left', border: 'none', background: 'none' }}
                                >
                                    <HiveCard hive={hive} />
                                </button>
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
                                                {actionItem.actionOfTheWeekMessage && (
                                                    <p>{actionItem.actionOfTheWeekMessage}</p>
                                                )}
                                                <h5>{formatActionType(actionItem.actionType)}</h5>
                                                {(() => {
                                                    switch (actionItem.actionType) {
                                                        case "ADD_EGGS_FRAME":
                                                        case "ADD_HONEY_FRAME":
                                                        case "SPLIT_HIVE":
                                                            return actionItem.data.hiveIds.map((hiveId, hiveIndex) => (
                                                                <div key={`${actionIndex}-${hiveIndex}`} className="form-check">
                                                                    <input
                                                                        type="checkbox"
                                                                        className="form-check-input"
                                                                        id={`hive-${actionItem.actionType}-${hiveId}`}
                                                                        checked={selectedActions[`${actionItem.actionType}-${hiveId}`] || false}
                                                                        onChange={() => handleCheckboxChange(actionItem.actionType, hiveId)}
                                                                    />
                                                                    <label className="form-check-label" htmlFor={`hive-${actionItem.actionType}-${hiveId}`}>
                                                                        Hive {hiveId}
                                                                    </label>
                                                                </div>
                                                            ));

                                                        case "MOVE_EGGS_FRAME":
                                                            return actionItem.data.hiveIdPairs && Array.isArray(actionItem.data.hiveIdPairs) ? (
                                                                actionItem.data.hiveIdPairs.map((pair, pairIndex) => {
                                                                    const checkboxKey = `${actionItem.actionType}-${pair[0]}-${pair[1]}`;
                                                                    const isInactive = Object.keys(selectedActions).some(key =>
                                                                        key.startsWith(`${actionItem.actionType}-${pair[0]}-`) && selectedActions[key]
                                                                    );
                                                                    return (
                                                                        <div key={pairIndex}>
                                                                            <label>
                                                                                <input
                                                                                    type="checkbox"
                                                                                    checked={!!selectedActions[checkboxKey]}
                                                                                    onChange={() => {
                                                                                        if (!isInactive) {
                                                                                            setSelectedActions((prevSelectedActions) => {
                                                                                                const newSelectedActions = { ...prevSelectedActions };
                                                                                                Object.keys(newSelectedActions).forEach(key => {
                                                                                                    if (key.startsWith(`${actionItem.actionType}-${pair[0]}-`)) {
                                                                                                        delete newSelectedActions[key];
                                                                                                    }
                                                                                                });
                                                                                                newSelectedActions[checkboxKey] = !prevSelectedActions[checkboxKey];
                                                                                                return newSelectedActions;
                                                                                            });
                                                                                        }
                                                                                    }}
                                                                                    disabled={isInactive}
                                                                                />
                                                                                Move frame from hive {pair[0]} to hive {pair[1]}
                                                                            </label>
                                                                        </div>
                                                                    );
                                                                })
                                                            ) : null;

                                                        case "FEED_BEES":
                                                        case "INSECT_CONTROL":
                                                            return (
                                                                <div>
                                                                    {actionItem.data.hiveIds && Array.isArray(actionItem.data.hiveIds) && actionItem.data.hiveIds.length > 0 && (
                                                                        <p>In all your hives: {actionItem.data.hiveIds.join(', ')}</p>
                                                                    )}
                                                                    <div className="form-check">
                                                                        <label>
                                                                            <input
                                                                                type="radio"
                                                                                name={`yesNo-${actionItem.actionType}`}
                                                                                value="yes"
                                                                                checked={selectedActions[actionItem.actionType] === "yes"}
                                                                                onChange={() => handleYesNoChange(actionItem.actionType, "yes")}
                                                                            />
                                                                            Yes
                                                                        </label>
                                                                        <label>
                                                                            <input
                                                                                type="radio"
                                                                                name={`yesNo-${actionItem.actionType}`}
                                                                                value="no"
                                                                                checked={selectedActions[actionItem.actionType] === "no"}
                                                                                onChange={() => handleYesNoChange(actionItem.actionType, "no")}
                                                                            />
                                                                            No
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            );

                                                        case "HARVEST_HONEY":
                                                            return (
                                                                <p>
                                                                    {actionItem.data.hiveIds.map((hiveId, hiveIndex) => (
                                                                        <span key={`${actionIndex}-${hiveIndex}`}>
                                                                            Hive {hiveId}{hiveIndex < actionItem.data.hiveIds.length - 1 ? ', ' : ''}
                                                                        </span>
                                                                    ))}
                                                                </p>
                                                            );

                                                        case "HIBERNATE":
                                                            return (
                                                                <p>
                                                                    {actionItem.data.hiveIds.map((hiveId, hiveIndex) => (
                                                                        <span key={`${actionIndex}-${hiveIndex}`}>
                                                                            Your hive with id {hiveId} died during last winter
                                                                        </span>
                                                                    ))}
                                                                </p>
                                                            );

                                                        default:
                                                            return null;
                                                    }
                                                })()}
                                                <hr />
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
                    <div className="d-flex flex-column align-items-center">
                        <p className="btn-custom p-custom mb-2">Date: {gameData ? gameData.currentDate : 'Loading...'}</p>
                        <p className="btn-custom p-custom mb-2">
                            Temp: {gameData && gameData.temperature !== undefined ? gameData.temperature.toFixed(2) : '0.00'}
                        </p>
                        <p className="btn-custom p-custom mb-2">
                            Wind speed: {gameData && gameData.windSpeed !== undefined ? gameData.windSpeed.toFixed(2) : '0.00'}
                        </p>

                        <p className="btn-custom p-custom mb-2">
                            Precipitation: {gameData && gameData.precipitation !== undefined ? gameData.precipitation.toFixed(2) : '0'}
                        </p>

                        <p className="btn-custom p-custom mb-2">
                            Total honey harvested: {gameData && gameData.totalKgOfHoneyHarvested !== undefined ? gameData.totalKgOfHoneyHarvested.toFixed(2) : 'Loading'}
                        </p>

                        <p className="btn-custom p-custom mb-2">Money in the bank: {gameData && gameData.moneyInTheBank ? gameData.moneyInTheBank.toFixed(2) : 'Loading...'}</p>
                        <img src={flowerImage} alt="Flower based on date" className="img-custom mb-2" />

                        <button className="btn btn-custom-iterate p-custom-iterate mb-2" onClick={handleIterateWeek}>Iterate one week</button>
                        
                        <button
                            className="btn btn-custom p-custom mb-2"
                            onClick={() => navigate(`/sell-honey?gameId=${gameId}`)}
                        >
                            Sell Honey
                        </button>


                        <div style={{ position: "relative", display: "inline-block", textAlign: "center" }}>
                            <button
                                onClick={handleBuyHivesClick}
                                className="btn btn-custom p-custom mb-2"
                                disabled={!isMarchOrApril()}
                            >
                                Buy Hives
                            </button>
                            {!isMarchOrApril() && (
                                <p className="error-message" style={{ color: "red", marginTop: "5px" }}>
                                    You can only buy hives in March or April.
                                </p>
                            )}
                        </div>
                       
                        {showBuyHivesForm && (
                            <BuyHivesModal
                        
                                hivesToBuy={hivesToBuy}
                                maxHives={maxHives}
                                availableFunds={gameData.moneyInTheBank}
                                onClose={() => setShowBuyHivesForm(false)}
                                onSubmit={handleSubmitHivesPurchase}
                                error={error}
                                onChangeHivesToBuy={handleHivesToBuyChange}
                            />
                        )}
                        

                        <button className="btn btn-danger btn-custom mb-2" onClick={() => navigate('/')}>Exit</button>
                    </div>
                </div>
            </div>
        </div>
    );
}
export default GameView;