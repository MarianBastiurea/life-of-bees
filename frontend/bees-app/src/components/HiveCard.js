const HiveCard = ({ hive }) => {
    return (
        <div className="card h-100 shadow-sm card-hover">
            <div className="card-body">
                <p>Hive ID: {hive.id}</p>
                <p>Queen's age: {hive.ageOfQueen}</p>
                <p>EggsFrame:{hive.eggsFrame}</p>
                <p>HoneyFrame:{hive.honeyFrame}</p>
                <p>Was split: {hive.itWasSplit ? 'Yes' : 'No'}</p>

            </div>
        </div>
    );
};

export default HiveCard;
