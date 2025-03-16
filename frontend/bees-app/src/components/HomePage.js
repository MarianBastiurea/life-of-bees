import React, { useState, useEffect } from 'react';
import '../App.css';
import NewGameModal from './CreateNewGame';
import ApiaryCardsRow from './ApiaryCardsRow';
import AuthModal from './AuthModal';
import { authenticateUser, registerUser, deleteGame } from './BeesApiService';
import { useNavigate } from 'react-router-dom';


const HomePage = () => {
    const [showPublicModal, setShowPublicModal] = useState(false);
    const [showPrivateModal, setShowPrivateModal] = useState(false);
    const [showAuthModal, setShowAuthModal] = useState(false);
    const [isSignUp, setIsSignUp] = useState(false);
    const [authMessage, setAuthMessage] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [username, setUsername] = useState(null);
    const [gameType, setGameType] = useState(null);
    const [activeTab, setActiveTab] = useState("Public Game");
    const [games, setGames] = useState([]);

    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
    });

    useEffect(() => {
        const storedToken = localStorage.getItem('authToken');
        const storedUsername = localStorage.getItem('username');

        console.log('Rehydrating auth state:', { storedToken, storedUsername });

        if (storedToken && storedUsername) {
            setIsAuthenticated(storedUsername !== 'JohnDoe');
            setUsername(storedUsername);
        } else {
            const autoLogin = async () => {
                try {
                    const username = 'JohnDoe';
                    const password = 'JohnDoe123';
                    const response = await authenticateUser({ username, password });

                    localStorage.setItem('authToken', response.token);
                    localStorage.setItem('userId', response.userId);
                    localStorage.setItem('username', username);

                    setUsername(username);
                } catch (error) {
                    console.error('Error in auto SignIn:', error);
                }
            };

            autoLogin();
        }
    }, []);


    const handlePublicGameClick = () => {
        setGameType("public");
        if (isAuthenticated) {
            setShowPublicModal(true);
        } else {
            const username = 'JohnDoe';
            const password = 'JohnDoe123';
            handleSignIn(username, password);
            setShowPublicModal(true);
        }
    };

    const handlePrivateGameClick = () => {
        setGameType("private");
        if (isAuthenticated) setShowPrivateModal(true);
    };

    const handleAuthClick = (signUp) => {
        console.log("handleAuthClick called with signUp:", signUp);
        setIsSignUp(signUp);
        setFormData({
            username: '',
            password: '',
            confirmPassword: '',
        });

        if (!isAuthenticated) {
            console.log("AuthModal should open now in SignUp");
            setShowAuthModal(true);
        }
    };

    const handleLogout = async () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userId');
        localStorage.removeItem('username');
        setIsAuthenticated(false);
        setUsername(null);
        try {
            const username = 'JohnDoe';
            const password = 'JohnDoe123';
            const response = await authenticateUser({ username, password });

            localStorage.setItem('authToken', response.token);
            localStorage.setItem('userId', response.userId);
            localStorage.setItem('username', username);

            setUsername(username);
        } catch (error) {
            console.error('Error in auto SignIn:', error);
        }
    };

    const handleCloseModal = () => {
        setShowPublicModal(false);
        setShowPrivateModal(false);
        setShowAuthModal(false);
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSignIn = async (username, password) => {
        try {
            const response = await authenticateUser({ username, password });
            console.log('Response from server in SignIn:', response);
            localStorage.setItem('authToken', response.token);
            localStorage.setItem('userId', response.userId);
            localStorage.setItem('username', username);
            console.log('Username saved to localStorage in SignIn:', username);
            console.log('User signed in signIn:', { userId, username });
            if (username !== 'JohnDoe') {
                setIsAuthenticated(true);
            }
            setShowAuthModal(false);
            setUsername(username);
        } catch (error) {
            console.error('Error in SignIn:', error);
            setAuthMessage(error.response?.data || 'Failed to sign in. Please try again.');
            setIsAuthenticated(false);
        }
    };

    const handleSignUp = async (username, password) => {
        try {
            const response = await registerUser({ username, password });
            if (response.error) {
                setAuthMessage(response.error);
                setIsAuthenticated(false);
                return;
            }
            localStorage.setItem('authToken', response.token);
            localStorage.setItem('userId', response.userId);
            localStorage.setItem('username', username);
            console.log('Username saved to localStorage in SignUp:', username);
            console.log('User signed up:', { token: response.token, userId: response.userId, username });
            setIsAuthenticated(true);
            setShowAuthModal(false);
            setUsername(username);
        } catch (error) {
            console.error('Error in SignUp:', error);
            setAuthMessage(error.response?.data?.error || 'Failed to register. Please try again.');
            setIsAuthenticated(false);
        }
    };

    const handleTabClick = (tab) => {
        if (tab === "Private Game" && !isAuthenticated) return;
        setActiveTab(tab);
    };


    const userId = localStorage.getItem('userId');
    const navigate = useNavigate();

    const handleGameClick = (gameId) => {
        navigate(`/GameView/${gameId}`);
    };


    const handleDelete = async (gameId) => {
        try {
            await deleteGame(gameId);
            setGames((prevGames) => prevGames.filter((game) => game.id !== gameId));
            console.log(`Game with ID ${gameId} has been deleted.`);
        } catch (error) {
            console.error(`Error deleting game ${gameId}:`, error.message);
        }
    };

    useEffect(() => {
        console.log("showAuthModal changed:", showAuthModal);
    }, [showAuthModal]);


    return (
        <div className="container">
            <h1>Life of Bees</h1>
            <div className="d-flex gap-2 mb-3 justify-content-start align-items-center">
                <button
                    className="btn btn-primary btn-lg"
                    onClick={handlePublicGameClick}
                >
                    Create public game
                </button>

                <button
                    className="btn btn-primary btn-lg"
                    onClick={handlePrivateGameClick}
                    disabled={!isAuthenticated || username === "JohnDoe"}
                >
                    Create private game
                </button>

                <div className="d-flex gap-3 ms-auto align-items-center">
                    {username && <span className="hello-user">Hello, {username}!</span>}

                    {username && username !== "JohnDoe" ? (
                        <button className="btn btn-danger" onClick={handleLogout}>
                            Logout
                        </button>
                    ) : (
                        <button className="btn btn-success" onClick={() => handleAuthClick(false)}>
                            Sign In
                        </button>
                    )}
                </div>

            </div>
            <div className="pt-3">
                <ul className="nav nav-tabs pt-3">
                    <li className="nav-item">
                        <button
                            className={`nav-link ${activeTab === "Public Game" ? "active" : ""}`}
                            onClick={() => handleTabClick("Public Game")}
                        >
                            Public Game
                        </button>
                    </li>
                    <li className="nav-item">
                        <button
                            className={`nav-link ${activeTab === "Private Game" ? "active" : ""}`}
                            onClick={() => handleTabClick("Private Game")}
                            disabled={!isAuthenticated}
                        >
                            Private Game
                        </button>
                    </li>
                </ul>

                <div className="tab-content pt-3">
                    {activeTab === "Public Game" && (
                        <ApiaryCardsRow
                            gameType="public"
                            onGameClick={(gameId) => navigate(`/GameView/${gameId}`, { state: { isPublic: true } })}
                            handleDelete={handleDelete}
                        />

                    )}

                    {activeTab === "Private Game" && isAuthenticated && (
                        <div>
                            <ApiaryCardsRow
                                gameType="private"
                                isAuthenticated={isAuthenticated}
                                userId={userId}
                                onGameClick={handleGameClick}
                                handleDelete={handleDelete}
                            />
                        </div>
                    )}
                </div>

            </div>

            {showPublicModal && (
                <NewGameModal
                    gameType="public"
                    userId={userId}
                    username={username}
                    handleClose={handleCloseModal}
                />
            )}

            {showPrivateModal && (
                <NewGameModal
                    gameType="private"
                    userId={userId}
                    username={username}
                    handleClose={handleCloseModal}
                />
            )}

            {showAuthModal && (
                <AuthModal
                    handleClose={handleCloseModal}
                    handleSubmit={(username, password) =>
                        isSignUp ? handleSignUp(username, password) : handleSignIn(username, password)
                    }
                    handleInputChange={handleInputChange}
                    formData={formData}
                    isSignUp={isSignUp}
                    errorMessage={authMessage}
                    setIsAuthenticated={setIsAuthenticated}
                    setUsername={setUsername}
                    authMessage={authMessage}
                    setIsSignUp={setIsSignUp}
                    setFormData={setFormData}
                />
            )}

        </div>
    );
};

export default HomePage;