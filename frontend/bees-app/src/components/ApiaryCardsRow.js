import React, { useEffect, useState } from 'react';
import { getJohnDoeGames, getGamesForUserByType } from './BeesApiService';
import { useNavigate } from 'react-router-dom';

const ApiaryCardsRow = ({ isAuthenticated, userId, gameType, onGameClick }) => {
    const [games, setGames] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        console.log("isAuthenticated:", isAuthenticated);
        console.log("userId:", userId);
        console.log("gameType:", gameType);

        const fetchGames = async () => {
            try {
                let recentGames;
                console.log("Fetching games with params:", { isAuthenticated, userId, gameType });

                if (isAuthenticated && gameType && userId) {
                    console.log("Calling getGamesForUserByType for authenticated userId:", userId);
                    recentGames = await getGamesForUserByType(userId, gameType);
                } else {
                    console.log("Calling getJohnDoeGames for anonymous user.");
                    recentGames = await getJohnDoeGames();
                }
                console.log("Recent games fetched:", recentGames);
                setGames(recentGames);
            } catch (error) {
                console.error('Eroare la încărcarea jocurilor recente:', error);
            }
        };

        fetchGames();
    }, [isAuthenticated, userId, gameType]);

    const chunkArray = (array, chunkSize) => {
        const chunks = [];
        for (let i = 0; i < array.length; i += chunkSize) {
            chunks.push(array.slice(i, i + chunkSize));
        }
        return chunks;
    };

    const chunkedGames = chunkArray(games, 6);

    const handleGameClick = (gameId) => {
        console.log('Navigating to GameView with ID:', gameId);
        navigate(`/GameView/${gameId}`);
    };
    


    return (
        <div className="container">
            {chunkedGames.map((group, index) => (
                <div className="row" key={index}>
                    {group.map((game, idx) => (
                        <div
                            className="col-md-4 col-lg-3 mb-3"
                            key={idx}
                            onClick={() => handleGameClick(game.gameId)} 
                        >
                            <div
                                className="card h-100 text-center shadow-sm card-hover"
                                style={{ cursor: "pointer" }}
                            >
                                <div className="card-body">
                                    <h5 className="card-title">{game.gameName}</h5>
                                    <p className="card-text">Location: {game.location}</p>
                                    <p className="card-text">Hives: {game.hivesNumber}</p>
                                    <p className="card-text">Funds: {game.moneyInTheBank}$</p>
                                    <p className="card-text">Honey: {game.totalKgOfHoneyHarvested} kg</p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            ))}
        </div>
    );
};

export default ApiaryCardsRow;
