import React, { useState, useEffect, useMemo } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router-dom';
import '../App.css';
import HiveCard from './HiveCard';
import { getGame, iterateWeek, buyHives, getPublicGame, iteratePublicGameOneWeek, buyHivesForPublicGames } from './BeesApiService';
import rapeseedFlower from '../flowersPhotos/rapeseed-flower.jpg';
import wildFlower from '../flowersPhotos/wild-flower.jpg';
import acaciaFlower from '../flowersPhotos/acacia-flower.jpg';
import lindenFlower from '../flowersPhotos/linden-flower.jpg';
import sunFlower from '../flowersPhotos/sun-flower.jpg';
import falseIndigoFlower from '../flowersPhotos/false-indigo-flower.jpg';
import BuyHivesModal from './BuyHivesModal';
import HappyBees from '../flowersPhotos/HappyBees.jpg';


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
    const [removedHiveMessage, setRemovedHiveMessage] = useState("");
    const endGameDate = new Date();
    const [gameEnded, setGameEnded] = useState(false);
    const location = useLocation();
    const isPublic = location.state?.isPublic || false;
    const [disabledActions, setDisabledActions] = useState({});


    useEffect(() => {
        if (!gameId) {
            console.error('Game ID is missing!');
            return;
        }

        async function fetchGameData() {
            try {
                console.log('data for  ID:', gameId);
                const data = await getGame(gameId);
                console.log('data from fetchGameData from GameView:', data);
                const currentDateStr = data.currentDate;
                console.log('currentDate(string):', currentDateStr);
                const currentDate = new Date(currentDateStr);
                console.log('Obiect Date:', currentDate);
                setMonth(currentDate.getMonth() + 1);
                console.log('Month:', currentDate.getMonth() + 1);

                setDay(currentDate.getDate());
                console.log('Day:', currentDate.getDate());
                setGameData(data);
                setUpdatedGameData(data);

            } catch (error) {
                console.error('No receiving data in GameView:', error);
            } finally {
                setLoading(false);
            }
        }

        fetchGameData();
    }, [gameId]);

    const handleYesNoChange = (actionType, response) => {
        setSelectedActions((prevSelectedActions) => ({
            ...prevSelectedActions,
            [actionType]: response,
        }));
    };
    const handleCheckboxChange = (actionType, hiveId) => {
        setSelectedActions((prevSelectedActions) => ({
            ...prevSelectedActions,
            [`${actionType}-${hiveId}`]: !prevSelectedActions[`${actionType}-${hiveId}`],
        }));
    };




    const showRemovedHiveMessage = (removedHiveId) => {
        setRemovedHiveMessage(`Hive with ID ${removedHiveId} has been removed.`);
    };


    const handleIterateWeek = async () => {
        if (!gameId) {
            console.error("Game ID is missing!");
            return;
        }

        try {
            const actionsByType = Object.keys(selectedActions)
                .filter(key => selectedActions[key])
                .reduce((acc, key) => {
                    const [actionType, sourceHiveId, destinationHiveId] = key.split('-');
                    if (!acc[actionType]) {
                        acc[actionType] = [];
                    }

                    if (['ADD_EGGS_FRAME', 'ADD_HONEY_FRAME', 'SPLIT_HIVE', 'HARVEST_HONEY'].includes(actionType)) {
                        acc[actionType].push(parseInt(sourceHiveId));
                    } else if (actionType === 'MOVE_EGGS_FRAME' && sourceHiveId && destinationHiveId) {
                        acc[actionType].push([
                            parseInt(sourceHiveId),
                            parseInt(destinationHiveId),
                        ]);
                    } else if (['INSECT_CONTROL', 'FEED_BEES'].includes(actionType)) {
                        acc[actionType] = selectedActions[key];
                    } else {
                        acc[actionType].push(selectedActions[key]);
                    }

                    return acc;
                }, {});

            const actionsOfTheWeek = {
                actions: actionsByType,
            };

            console.log("Actions of the week being sent:", actionsOfTheWeek);

            const response = await iterateWeek(gameId, actionsOfTheWeek);

            console.log("Response from backend:", response);
            if (response) {
                const updatedGameData = processBackendResponse(response);

                console.log("Week iterated successfully!");
                setGameData(updatedGameData);
                setUpdatedGameData(updatedGameData);
                setSelectedActions({});

                if (response.removedHiveId) {
                    showRemovedHiveMessage(response.removedHiveId);
                } else {
                    setRemovedHiveMessage("");
                }

                const dateObject = new Date(updatedGameData.currentDate);
                setMonth(dateObject.getMonth() + 1);
                setDay(dateObject.getDate());

                if (dateObject >= endGameDate) {
                    setGameEnded(true);
                }
            }
        } catch (error) {
            console.error("Error iterating week:", error);
        }
    };


    const processBackendResponse = (response) => {

        const updatedData = {
            ...response,
        };

        return updatedData;
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
                const data = await getGame(gameId);

                setGameData(data);
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

    const insuficientFunds = () => {
        return gameData && gameData.moneyInTheBank < 500;
    };


    const goToHiveHistory = (hiveId) => {
        if (!gameId) {
            console.error("gameId is missing");
            return;
        }
        console.log("Navigating to HiveHistory with gameId:", gameId, "and hiveId:", hiveId);
        navigate('/HiveHistory', { state: { gameId: gameId, hiveId: hiveId } });
    };

    const goToApiaryHistory = (gameId) => {
        if (!gameId) {
            console.error("gameId is missing");
            return;
        }
        console.log("Navigating to ApiaryHistory with gameId:", gameId);
        navigate('/ApiaryHistory', { state: { gameId: gameId } });
    };

    return (
        <div>
            {gameEnded ? (
                <div style={{ textAlign: "center" }}>
                    <h1>End Game!</h1>
                    <img
                        src={HappyBees}
                        alt="Happy Bees"
                        style={{ width: "400px", height: "auto" }}
                    />
                </div>
            ) : (
                <div className="body-gameView">
                    <div className="row">
                        <div className="col-md-6">
                            <div className="row">
                                {gameData && gameData.hives && gameData.hives.length > 0 ? (
                                    gameData.hives.map((hive, index) => (
                                        <button
                                            key={hive.id}
                                            className="col-md-6 mb-3 btn btn-outline-primary"
                                            onClick={() => {
                                                console.log("Navigating to HiveHistory with:", { gameId });
                                                goToHiveHistory(hive.id);
                                            }}
                                            style={{ cursor: 'pointer', textAlign: 'left', border: 'none', background: 'none' }}
                                        >
                                            <HiveCard hive={hive} />
                                        </button>

                                    ))
                                ) : (
                                    <p>No hives available or data not loaded yet.</p>
                                )}
                            </div>

                            <button
                                className="btn btn-custom-iterate p-custom-iterate mb-2"
                                onClick={() => goToApiaryHistory(gameId)}
                                style={{ cursor: 'pointer', textAlign: 'left', border: 'none', background: 'none' }}
                            >
                                Game History
                            </button>
                        </div>

                        <div className="col-md-3">
                            <div className="card mb-3">
                                <div className="card-body">
                                    {console.log("Updated game data in actions column:", updatedGameData)}

                                    {removedHiveMessage && <p>{removedHiveMessage}</p>}
                                    {updatedGameData && updatedGameData.actions && updatedGameData.actions.actions ? (
                                        Object.keys(updatedGameData.actions.actions).length > 0 ? (
                                            <div>
                                                <p>Actions of the week:</p>
                                                <form>
                                                    {Object.keys(updatedGameData.actions.actions).map((actionType) => (
                                                        <div key={actionType}>
                                                            <h5>{formatActionType(actionType)}</h5>
                                                            {(() => {
                                                                const actionData = updatedGameData.actions.actions[actionType];
                                                                switch (actionType) {
                                                                    case "ADD_EGGS_FRAME":
                                                                    case "ADD_HONEY_FRAME":
                                                                    case "SPLIT_HIVE":
                                                                    case "HARVEST_HONEY":
                                                                        return actionData.map((hiveId, index) => (
                                                                            <div key={`${actionType}-${index}`} className="form-check">
                                                                                <input
                                                                                    type="checkbox"
                                                                                    className="form-check-input"
                                                                                    id={`hive-${actionType}-${hiveId}`}
                                                                                    checked={selectedActions[`${actionType}-${hiveId}`] || false}
                                                                                    onChange={() => handleCheckboxChange(actionType, hiveId)}
                                                                                />
                                                                                <label className="form-check-label" htmlFor={`hive-${actionType}-${hiveId}`}>
                                                                                    Hive {hiveId}
                                                                                </label>
                                                                            </div>
                                                                        ));

                                                                    case "MOVE_EGGS_FRAME":
                                                                        return actionData.map((pair, index) => {
                                                                            const checkboxKey = `${actionType}-${pair.sourceHiveId}-${pair.destinationHiveId}`;
                                                                            const isInactive = Object.keys(selectedActions).some(key =>
                                                                                key.startsWith(`${actionType}-${pair.sourceHiveId}-`) && selectedActions[key]
                                                                            );
                                                                            return (
                                                                                <div key={`${actionType}-${index}`}>
                                                                                    <label>
                                                                                        <input
                                                                                            type="checkbox"
                                                                                            checked={!!selectedActions[checkboxKey]}
                                                                                            onChange={() => {
                                                                                                if (!isInactive) {
                                                                                                    setSelectedActions((prevSelectedActions) => {
                                                                                                        const newSelectedActions = { ...prevSelectedActions };
                                                                                                        Object.keys(newSelectedActions).forEach((key) => {
                                                                                                            if (key.startsWith(`${actionType}-${pair[0]}-`)) {
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
                                                                                        Move frame from hive {pair.sourceHiveId} to hive {pair.destinationHiveId}

                                                                                    </label>
                                                                                </div>
                                                                            );
                                                                        });

                                                                    case "FEED_BEES":
                                                                    case "INSECT_CONTROL":
                                                                        const isDisabled = gameData && gameData.moneyInTheBank < (gameData.hives.length * 10);


                                                                        return (
                                                                            <div>
                                                                                <div className="form-check">
                                                                                    <label>
                                                                                        <input
                                                                                            type="radio"
                                                                                            name={`yesNo-${actionType}`}
                                                                                            value="yes"
                                                                                            checked={selectedActions[actionType] === "yes"}
                                                                                            onChange={() => handleYesNoChange(actionType, "yes")}
                                                                                            disabled={isDisabled}
                                                                                        />
                                                                                        Yes
                                                                                    </label>
                                                                                    <label>
                                                                                        <input
                                                                                            type="radio"
                                                                                            name={`yesNo-${actionType}`}
                                                                                            value="no"
                                                                                            checked={selectedActions[actionType] === "no"}
                                                                                            onChange={() => handleYesNoChange(actionType, "no")}
                                                                                            disabled={isDisabled}
                                                                                        />
                                                                                        No
                                                                                    </label>
                                                                                </div>
                                                                            </div>
                                                                        );


                                                                    default:
                                                                        return null;
                                                                }
                                                            })()}
                                                            <hr />
                                                        </div>
                                                    ))}
                                                </form>
                                            </div>
                                        ) : (
                                            <p>Weekly checking</p>
                                        )
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

                                <p className="btn-custom p-custom mb-2">Money in the bank: {gameData && gameData.moneyInTheBank ? gameData.moneyInTheBank.toFixed(2) : ''}</p>

                                {insuficientFunds() && (
                                    <p className="error-message" style={{ color: "red", marginTop: "5px" }}>
                                        You are nearing insufficient funds.
                                        Please sell honey to perform InsectControl or FeedBees.
                                    </p>
                                )}

                                <img src={flowerImage} alt="Flower based on date" className="img-custom mb-2" />

                                <button className="btn btn-custom-iterate p-custom-iterate mb-2" onClick={handleIterateWeek}>Iterate one week</button>

                                <button
                                    className="btn btn-custom p-custom mb-2"
                                    onClick={() => navigate('/sell-honey', { state: { gameId } })}
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
            )}
        </div>
    );
}
export default GameView;