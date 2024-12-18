import React, { useState, useEffect } from 'react';
import '../App.css';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { sendSellHoneyQuantities, getHoneyQuantities } from './BeesApiService';

const RowHeader = () => (
    <div className="row-text">
        <p className="btn-custom-sell mb-2">Honey Type</p>
        <p className="btn-custom-sell mb-2">Quantity[kg]</p>
        <p className="btn-custom-sell mb-2">Price[$/kg]</p>
        <p className="btn-custom-sell mb-2">You sell</p>
        <p className="btn-custom-sell mb-2">Value</p>
    </div>
);

const RowText = ({ honeyType, quantity, price, onQuantityChange }) => {
    const [sellQuantity, setSellQuantity] = useState(0);

    const handleInputChange = (event) => {
        const value = Math.max(0, Math.min(Number(event.target.value), quantity)) || 0;
        setSellQuantity(value);
        onQuantityChange(value, honeyType, price);
    };

    const totalValue = (sellQuantity * price).toFixed(2);

    return (
        <div className="row-text">
            <p className="btn-custom-sell mb-2">{honeyType}</p>
            <p className="btn-custom-sell mb-2">{quantity}</p>
            <p className="btn-custom-sell mb-2">{price}</p>
            <form>
                <input
                    className="btn-custom-sell mb-2"
                    type="number"
                    min="0"
                    max={quantity}
                    value={sellQuantity || ''}
                    onChange={handleInputChange}
                    style={{ width: '150px' }}
                />
            </form>

            <p className="btn-custom-sell mb-2">${totalValue}</p>
        </div>
    );
};
const SellHoney = () => {
    const [searchParams] = useSearchParams();
    const gameId = searchParams.get('gameId');
    const [honeyData, setHoneyData] = useState([]);
    const [soldValues, setSoldValues] = useState({});
    const [soldValueTotals, setSoldValueTotals] = useState({});
    const [totalHoneyQuantity, setTotalHoneyQuantity] = useState(0);
    const navigate = useNavigate();
    console.log('acesta e gameId: ',gameId);

    useEffect(() => {
        const fetchHoneyData = async () => {
            if (!gameId) {
                console.error('Missing gameId');
                return;
            }

            try {
                const data = await getHoneyQuantities(gameId);

                const parsedData = Object.entries(data).map(([honeyType, quantity]) => ({
                    honeyType,
                    quantity,
                }));
                setHoneyData(parsedData);

                const totalQuantity = parsedData.reduce((acc, item) => acc + item.quantity, 0);
                setTotalHoneyQuantity(totalQuantity);
            } catch (error) {
                console.error('Error fetching honey data:', error);
            }
        };

        fetchHoneyData();
    }, [gameId]);


    const updateTotalSoldValue = (sellQuantity, honeyType, price) => {
        setSoldValues((prevSoldValues) => ({
            ...prevSoldValues,
            [honeyType]: sellQuantity || 0,
        }));
        setSoldValueTotals((prevSoldValueTotals) => ({
            ...prevSoldValueTotals,
            [honeyType]: (sellQuantity * price).toFixed(2),
        }));
    };

    const totalSoldValue = Object.values(soldValueTotals)
        .reduce((acc, val) => acc + Number(val), 0)
        .toFixed(2);



    const handleSubmit = async () => {
        const formattedSoldData = Object.entries(soldValues)
            .filter(([_, quantity]) => quantity > 0)
            .reduce((acc, [honeyType, quantity]) => {
                acc[honeyType] = parseFloat(quantity);
                return acc;
            }, {});

        try {

            const payload = {
                gameId,
                soldData: formattedSoldData,
                totalValue: parseFloat(totalSoldValue)
            };

            console.log('Payload trimis din SellHoney:', JSON.stringify(payload, null, 2));

            await sendSellHoneyQuantities.updateHoneyStock(gameId, formattedSoldData, parseFloat(totalSoldValue)); // Transmitem gameId
            navigate('/gameView', {
                state: { gameId },
            });
        } catch (error) {
            console.error('Error submitting total sold value:', error);
        }
    };


    return (
        <div className="body-sell">
            <h3>Total Honey in stock: {totalHoneyQuantity.toFixed(2)} kg</h3>
            <div className="container" style={{ marginTop: '50px' }}>
                <RowHeader />
                {honeyData.map(({ honeyType, quantity }) => (
                    <RowText
                        className="btn-custom-sell mb-2"
                        key={honeyType}
                        honeyType={honeyType}
                        quantity={quantity.toFixed(2)}
                        price={honeyType === "acacia" ? 6 : 3}
                        onQuantityChange={updateTotalSoldValue}
                    />
                ))}
            </div>
            <h3>Total value of honey sold: ${totalSoldValue}</h3>
            <button className="btn btn-primary mb-3" onClick={handleSubmit}>Submit</button>
            <button className="btn btn-danger button-right-bottom" onClick={() => navigate('/gameView', { state: { gameId } })}>Back</button>
        </div>
    );
};

export default SellHoney;