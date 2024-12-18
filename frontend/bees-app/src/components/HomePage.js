import React, { useState } from 'react';
import '../App.css';
import NewGameModal from './CreateNewGame';
import ApiaryCardsRow from './ApiaryCardsRow';
import AuthModal from './AuthModal';
import { authenticateUser, registerUser } from './BeesApiService';
import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';
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

    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
    });

    const handlePublicGameClick = () => {
        setGameType("public");
        if (isAuthenticated) {
            setShowPublicModal(true);
        } else {
            const username = 'JohnDoe';
            const password = 'JohnDoe123';
            handleSignUp(username, password);
            setShowPublicModal(true);
        }
    };

    const handlePrivateGameClick = () => {
        setGameType("private");
        if (isAuthenticated) setShowPrivateModal(true);
    };

    const handleAuthClick = (signUp) => {
        setIsSignUp(signUp);
        setFormData({
            username: '',
            password: '',
            confirmPassword: '',
        });
        setShowAuthModal(true);
    };

    const handleLogout = () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userId');
        setIsAuthenticated(false);
        setUsername(null);
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

            localStorage.setItem('authToken', response.token);
            localStorage.setItem('userId', response.userId);
            setIsAuthenticated(true);
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
            const { token, userId } = await registerUser({ username, password });

            localStorage.setItem('authToken', token);
            localStorage.setItem('userId', userId);
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
        setActiveTab(tab);
    };

    const userId = localStorage.getItem('userId');
    const navigate = useNavigate();

    const handleGameClick = (gameId) => {
        navigate(`/GameView/${gameId}`);
    };

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
                    className="btn btn-secondary btn-lg"
                    onClick={handlePrivateGameClick}
                    disabled={!isAuthenticated}
                >
                    Create private game
                </button>
                {isAuthenticated ? (
                    <div className="d-flex gap-3 ms-auto align-items-center">
                        <span className="hello-user">Hello, {username}!</span>
                        <button className="btn btn-danger" onClick={handleLogout}>
                            Logout
                        </button>
                    </div>
                ) : (
                    <div className="d-flex gap-3 ms-auto">
                        <button className="btn btn-success" onClick={() => handleAuthClick(false)}>
                            Sign In
                        </button>
                    </div>
                )}
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
                        >
                            Private Game
                        </button>
                    </li>
                    <li className="nav-item">
                        <button
                            className={`nav-link ${activeTab === "Map" ? "active" : ""}`}
                            onClick={() => handleTabClick("Map")}
                        >
                            Map
                        </button>
                    </li>
                </ul>

                    <div className="tab-content pt-3">
                        {activeTab === "Public Game" && (
                                <ApiaryCardsRow
                                    gameType="public"
                                    isAuthenticated={isAuthenticated}
                                    userId={userId}
                                    onGameClick={handleGameClick}
                                />
                        )}

                        {activeTab === "Private Game" && isAuthenticated && (
                                <div>
                                    <ApiaryCardsRow
                                        gameType="private"
                                        isAuthenticated={isAuthenticated}
                                        userId={userId}
                                        onGameClick={handleGameClick}
                                    />
                                </div>
                        )}

                        {activeTab === "Map" && <div>Map content goes here.</div>}
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
                />
            )}
            {authMessage && <p className="auth-message">{authMessage}</p>}
        </div>
    );
};

export default HomePage;
