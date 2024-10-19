const HiveCard = ({ hive }) => {
    return (
        <div className="card mb-2">
            <div className="card-body">
                <p>Hive ID: {hive.id}</p>
                <p>Queen's age: {hive.ageOfQueen}</p>
                <p>Bees: {hive.bees}</p>
                <p>EggsFrame:{hive.eggsFrame}</p>
                <p>HoneyFrame:{hive.honeyFrame}</p>
                <p>Honey: {hive.honeyType}</p>
                {hive.totalHoney > 0 && <p>Honey Harvested: {hive.totalHoney}</p>}
                <p>Was split: {hive.itWasSplit ? 'Yes' : 'No'}</p>
               
            </div>
        </div>
    );
};

export default HiveCard;
