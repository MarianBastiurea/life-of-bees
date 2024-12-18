import React, { useState, useEffect } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { getHiveHistory } from './BeesApiService';


const HiveHistory = () => {

    const location = useLocation();
    const { gameId, hiveId } = location.state || {}; // Extragem gameId din state-ul rutei

    console.log("hiveId from useParams:", hiveId); // Log pentru verificare
    console.log("gameId from location.state:", gameId);

    const [hiveHistoryData, setHiveHistoryData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        if (!gameId || !hiveId) {
            console.error('Missing gameId or hiveId:', { gameId, hiveId });
            setError('Missing gameId or hiveId.');
            setLoading(false);
            return;
        }

        const fetchHiveHistory = async () => {
            try {

                const data = await getHiveHistory(gameId, hiveId);
                console.log("Fetched HiveHistory Data:", data);
                setHiveHistoryData(data);
            } catch (err) {
                console.error('Error fetching hive history:', err);
                setError('Failed to fetch hive history.');
            } finally {
                setLoading(false);
            }
        };

        fetchHiveHistory();
    }, [gameId, hiveId]);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>{error}</div>;


    if (loading) return <div>Loading...</div>;
    if (error) return <div>{error}</div>;

    return (
        <div className="container mt-4">
            <h2>Hive {hiveId} History</h2>
            <table className="table table-striped">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Temperature</th>
                        <th>Wind Speed</th>
                        <th>Precipitation</th>
                        <th>Bees</th>
                        <th>Queen Age</th>
                        <th>Honey Type</th>
                        <th>Honey (kg)</th>
                        <th>Money in the Bank</th>
                        <th>Eggs Frame</th>
                        <th>Honey Frame</th>
                        <th>It Was Split</th>
                    </tr>
                </thead>
                <tbody>
                    {hiveHistoryData.length > 0 ? (
                        hiveHistoryData.map((entry, index) => (
                            <tr key={index}>
                                <td>{entry.currentDate}</td>
                                <td>{entry.weatherData.temperature}</td>
                                <td>{entry.weatherData.windSpeed}</td>
                                <td>{entry.weatherData.precipitation}</td>
                                <td>{entry.beesNumber}</td>
                                <td>{entry.queenAge}</td>
                                <td>{entry.honeyTypes && entry.honeyTypes.length > 0 ? entry.honeyTypes.join(', ') : 'N/A'}</td>
                                <td>{entry.kgOfHoney}</td>
                                <td>{entry.moneyInTheBank}</td>
                                <td>{entry.eggFramesNumber}</td>
                                <td>{entry.honeyFrameNumber}</td>
                                <td>{entry.itWasSplit ? 'Yes' : 'No'}</td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="12">No history available for this hive.</td>
                        </tr>
                    )}
                </tbody>

            </table>
            <button className="btn btn-danger button-right-bottom" onClick={() => navigate('/gameView', { state: { gameId } })}>Back</button>
    
        </div>
    );
};

export default HiveHistory;
