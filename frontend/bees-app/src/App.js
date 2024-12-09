import React, { useState, useEffect } from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './components/HomePage';
import GameView from './components/GameView';
import SellHoney from './components/SellHoney';
import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';

function App() {
  const [googleClientId, setGoogleClientId] = useState(null);

  // Funcția care gestionează login-ul cu Google
  const handleGoogleLogin = (response) => {
    fetch('/api/auth/oauth/google', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        token: response.credential, // Token-ul obținut de la Google
      }),
    })
    .then(res => res.json())
    .then(data => {
      console.log('User authenticated:', data);
      // Procesați răspunsul, de exemplu, stocați token-ul JWT în state sau localStorage
    })
    .catch(error => console.error('Error during Google OAuth login:', error));
  };

  // Fetch pentru obținerea Google Client ID din backend
  useEffect(() => {
    const fetchGoogleClientId = async () => {
      try {
        const response = await fetch('/api/config/google-client-id'); // Endpoint din backend
        const clientId = await response.text();
        setGoogleClientId(clientId);
      } catch (error) {
        console.error('Failed to fetch Google Client ID:', error);
      }
    };

    fetchGoogleClientId();
  }, []);

  // Afișează un loading dacă încă nu ai Google Client ID
  if (!googleClientId) {
    return <div>Loading...</div>;
  }

  return (
    <GoogleOAuthProvider clientId={googleClientId}>
      <Router>
        <div className="App">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/gameView" element={<GameView />} />
            <Route path="/sell-honey" element={<SellHoney />} />
          </Routes>

          {/* Adăugăm butonul GoogleLogin */}
          <GoogleLogin 
            onSuccess={handleGoogleLogin}
            onError={() => console.log('Login Failed')} 
          />
        </div>
      </Router>
    </GoogleOAuthProvider>
  );
}

export default App;
