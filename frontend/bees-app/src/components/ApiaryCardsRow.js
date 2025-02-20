import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getJohnDoeGames, getGamesForUserByType } from './BeesApiService';

const GameCard = ({ game, onDelete, onClick }) => (
    <div className="col-md-4 col-lg-3 mb-3">
        <div className="card h-100 text-center shadow-sm card-hover">
            <button
                className="btn btn-danger btn-sm delete-button"
                onClick={(e) => {
                    e.stopPropagation();
                    onDelete(game.gameId);
                }}
                style={{
                    position: "absolute",
                    top: "10px",
                    right: "10px",
                    zIndex: 1,
                }}
            >
                &times;
            </button>

            <div className="card-body" style={{ cursor: "pointer" }} onClick={() => onClick(game.gameId)}>
                <h5 className="card-title">{game.gameName}</h5>
                <p className="card-text">Location: {game.location}</p>
                <p className="card-text">Hives: {game.hivesNumber}</p>
                <p className="card-text">Funds: {game.moneyInTheBank}$</p>
                <p className="card-text">Honey: {game.totalKgOfHoneyHarvested} kg</p>
            </div>
        </div>
    </div>
);

const GameRow = ({ games, onDelete, onClick }) => (
    <div className="row">
        {games.map((game, idx) => (
            <GameCard key={idx} game={game} onDelete={onDelete} onClick={onClick} />
        ))}
    </div>
);

const ApiaryCardsRow = ({ isAuthenticated, userId, gameType, onGameClick, handleDelete }) => {
    const [games, setGames] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchGames = async () => {
            try {
                let recentGames;
                if (isAuthenticated && userId && gameType) {
                    recentGames = await getGamesForUserByType(userId, gameType);
                } else {
                    recentGames = await getJohnDoeGames();
                }
                console.log('these are games for user:', recentGames);
                setGames(recentGames);
            } catch (error) {
                console.error('Error loading recent games:', error);
            }
        };

        fetchGames();
    }, [isAuthenticated, userId, gameType]);


    const handleGameDelete = (gameId) => {
        handleDelete(gameId);
        setGames((prevGames) => prevGames.filter((g) => g.gameId !== gameId));
    };

    const chunkArray = (array, chunkSize) => {
        return Array.from({ length: Math.ceil(array.length / chunkSize) }, (_, i) =>
            array.slice(i * chunkSize, i * chunkSize + chunkSize)
        );
    };

    const chunkedGames = chunkArray(games, 6);

    return (
        <div className="container">
            {chunkedGames.map((group, index) => (
                <GameRow key={index} games={group} onDelete={handleGameDelete} onClick={onGameClick} />
            ))}
        </div>
    );
};

export default ApiaryCardsRow;
