
const BuyHivesModal = ({ hivesToBuy, maxHives, availableFunds, onClose, onSubmit, error, onChangeHivesToBuy }) => {
    return (
        <div className="modal show" style={{ display: 'block' }}>
            <div className="modal-dialog modal-sm">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">Buy Hives</h5>
                    </div>
                    <div className="modal-body">
                        <p>Available funds: ${availableFunds}</p>
                        <p>Hive's Price is 500$/each</p>
                        <p>Maximum hives you can buy: {maxHives}</p>
                        <input
                            type="number"
                            value={hivesToBuy}
                            onChange={onChangeHivesToBuy}
                            className="form-control"
                        />
                        {error && <p className="text-danger">{error}</p>}
                        <div className="d-flex justify-content-between">
                            <button type="button" className="btn btn-secondary" onClick={onClose}>
                                Cancel
                            </button>
                            <button type="button" className="btn btn-primary" onClick={onSubmit}>
                                Buy Hives
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default BuyHivesModal;
