import React, { useState } from 'react';
import '../App.css';
import NewGameModal from './CreateNewGame';
import ApiaryCardsRow from './ApiaryCardsRow';
import AuthModal from './AuthModal';
import { GoogleLogin } from '@react-oauth/google';
import { handleGitHubLogin } from './BeesApiService';

import { authenticateUser, registerUser } from './BeesApiService';

const HomePage = () => {
    const [showPublicModal, setShowPublicModal] = useState(false);
    const [showPrivateModal, setShowPrivateModal] = useState(false);
    const [showAuthModal, setShowAuthModal] = useState(false);
    const [isSignUp, setIsSignUp] = useState(false);
    const [authMessage, setAuthMessage] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [userName, setUserName] = useState(null);

    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
    });

    const handlePublicGameClick = () => {
        const username = 'JohnDoe';
        const password = 'JohnDoe123';
        handleSignUp(username, password);
        setShowPublicModal(true);
    };

    const handlePrivateGameClick = () => {
        if (isAuthenticated) setShowPrivateModal(true);
    };

    const handleAuthClick = (signUp) => {
        setIsSignUp(signUp);
        setShowAuthModal(true);
    };

    const handleLogout = () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userId');
        setIsAuthenticated(false);
        setUserName(null);
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

            setAuthMessage('User authenticated successfully!');
            setIsAuthenticated(true);
            setShowAuthModal(false);
            setUserName(username);
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

            setAuthMessage('User registered successfully!');
            setIsAuthenticated(true);
            setShowAuthModal(false);
            setUserName(username);
        } catch (error) {
            console.error('Error in SignUp:', error);
            setAuthMessage(error.response?.data?.error || 'Failed to register. Please try again.');
            setIsAuthenticated(false);
        }
    };



    const handleGoogleSuccess = (response) => {
        console.log('Google Login Success:', response);
        setIsAuthenticated(true);
        setUserName('GoogleUser');
    };


    const handleGoogleFailure = (error) => {
        console.error('Google Login Failure:', error);
        setAuthMessage('Google Login failed. Please try again.');
    };


    const handleGitHubSuccess = (response) => {
        console.log('GitHub Login Success:', response);
        setIsAuthenticated(true);
        setUserName('GitHubUser');
    };

    const handleGitHubFailure = (error) => {
        console.error('GitHub Login Failure:', error);
        setAuthMessage('GitHub Login failed. Please try again.');
    };


    const userId = localStorage.getItem('userId');

    return (
        <div className="container">
            <h1>Life of Bees</h1>
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
            <div className="pt-3">
                <ul className="nav nav-tabs pt-3">
                    <li className="nav-item">
                        <a className="nav-link active" href="#">List</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="#">Map</a>
                    </li>
                </ul>
            </div>

            <div className="auth-buttons-container">
                {isAuthenticated ? (
                    <div className="auth-logged-in">
                        <span className="hello-user">Hello {userName}!</span>
                        <button className="btn btn-danger" onClick={handleLogout}>
                            Logout
                        </button>
                    </div>
                ) : (
                    <div className="auth-buttons">
                        <button className="btn btn-success" onClick={() => handleAuthClick(false)}>
                            Sign In
                        </button>
                        <button className="btn btn-info" onClick={() => handleAuthClick(true)}>
                            Sign Up
                        </button>
                        <GoogleLogin
                            onSuccess={handleGoogleSuccess}
                            onError={handleGoogleFailure}
                        />
                        <button
                            onClick={handleGitHubLogin} 
                            className="btn btn-dark"
                        >
                            Login with GitHub
                        </button>
                    </div>
                )}
            </div>

            <ApiaryCardsRow />

            {showPublicModal && (
                <NewGameModal
                    isPublic={true}
                    userId={userId}
                    handleClose={handleCloseModal}
                />
            )}
            {showPrivateModal && (
                <NewGameModal
                    isPublic={false}
                    userId={userId}
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
                />
            )}
            {authMessage && <p className="auth-message">{authMessage}</p>}
        </div>
    );
};

export default HomePage;
