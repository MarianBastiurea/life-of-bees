import React, { useState, useEffect } from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './components/HomePage';
import GameView from './components/GameView';
import SellHoney from './components/SellHoney';
import { GoogleOAuthProvider } from '@react-oauth/google';
import HiveHistory from './components/HiveHistory';
import { getGoogleClientId, getGitHubClientId} from './components/BeesApiService';

function App() {
  const [googleClientId, setGoogleClientId] = useState(null);
  const [gitHubClientId, setGitHubClientId] = useState(null);

  useEffect(() => {
    const fetchGoogleClientId = async () => {
      try {
        const clientId = await getGoogleClientId();
        setGoogleClientId(clientId);
      } catch (error) {
        console.error('Failed to fetch Google Client ID:', error);
      }
    };

    fetchGoogleClientId();
  }, []);

  useEffect(() => {
  const fetchGitHubClientId = async () => {
    try {
      const clientId = await getGitHubClientId();
      setGitHubClientId(clientId);
    } catch (error) {
      console.error('Failed to fetch GitHub Client ID:', error);
    }
  };

  fetchGitHubClientId();
}, []);


  return (
    <GoogleOAuthProvider clientId={googleClientId}>
      <Router>
        <div className="App">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/gameView" element={<GameView />} />
            <Route path="/sell-honey" element={<SellHoney />} />
            <Route path="/HiveHistory" element={<HiveHistory />}/>
            <Route path="/gameView/:gameId" element={<GameView />} />
          </Routes>
        </div>
      </Router>
    </GoogleOAuthProvider>
  );
}

export default App;
