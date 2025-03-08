import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { getHiveHistory } from './BeesApiService';

const HiveHistory = () => {
    const location = useLocation();
    const { gameId, hiveId } = location.state || {};

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
                console.log('Data', data)
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

    return (
        <div className="container mt-4">
            <h2 className="text-center mb-4">Hive {hiveId} History</h2>
            <div className="table-responsive">
                <table className="table table-striped table-bordered">
                    <thead className="table-dark">
                        <tr>
                            <th>Date</th>
                            <th>Temperature</th>
                            <th>Wind Speed</th>
                            <th>Precipitation</th>
                            <th>Bees No</th>
                            <th>Queen Age (years)</th>
                            <th>Honey Type</th>
                            <th>Honey (kg)</th>
                            <th>Funds</th>
                            <th>Eggs Frame No</th>
                            <th>Honey Frame No</th>
                            <th>It Was Split</th>
                        </tr>
                    </thead>
                    <tbody>
                        {hiveHistoryData.length > 0 ? (
                            hiveHistoryData.map((entry, index) => {
                                const hive = entry.hive;
                                if (!hive) return null;
                                const beesNumber = hive.beesBatches?.beesBatches?.reduce((sum, batch) => sum + (batch || 0), 0) || 0;
                                const eggsFrameNo = hive.eggFrames.numberOfEggFrames;
                                const honeyFrameNo = hive.honeyFrames.honeyFrame.length;

                                return (
                                    <tr key={index}>
                                        <td>{entry.currentDate.localDate}</td>
                                        <td>{entry.weatherData.temperature}°C</td>
                                        <td>{entry.weatherData.windSpeed} km/h</td>
                                        <td>{entry.weatherData.precipitation} mm</td>
                                        <td>{beesNumber}</td>
                                        <td>{hive.queen.ageOfQueen}</td>
                                        <td>{hive.honeyBatches.map((batch, index) => (
                                            <p key={index}>{batch.honeyType}</p>
                                        ))
                                        }</td>
                                        <td>
                                            {hive.honeyBatches?.map((batch, index) => (
                                                <p key={index}>{batch.kgOfHoney.toFixed(2)}</p>
                                            )) || ""}
                                        </td>
                                        <td>${entry.moneyInTheBank}</td>
                                        <td>{eggsFrameNo}</td>
                                        <td>{honeyFrameNo}</td>
                                        <td>{hive.itWasSplit ? 'Yes' : 'No'}</td>
                                    </tr>
                                );
                            })
                        ) : (
                            <tr>
                                <td colSpan="12" className="text-center">No history available for this hive.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
            <button className="btn btn-danger mt-3 float-end" onClick={() => navigate('/gameView', { state: { gameId } })}>
                Back
            </button>
        </div>
    );
};

export default HiveHistory;
