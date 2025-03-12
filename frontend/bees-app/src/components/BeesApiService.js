import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || "http://localhost:8080";

const apiClient = axios.create({
    baseURL: API_BASE_URL + '/api',
});

export const getAuthToken = () => {
    return localStorage.getItem('authToken');
};

apiClient.interceptors.request.use(
    (config) => {
        const token = getAuthToken();
        console.log('This is Token from API Client', token)
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);


export const registerUser = async (registerData) => {
    try {
        const response = await apiClient.post('/auth/register', registerData);
        const { token, userId } = response.data;
        return { token, userId };
    } catch (error) {
        if (error.response?.status === 409) {
            return { error: error.response.data.error };
        }
        console.error('Error in registerUser:', error.response?.data || error.message);
        throw error;
    }
};


export const getGoogleClientId = async () => {
    try {
        const response = await apiClient.get('/auth/google-client-id');
        console.log('Google Id :', response.data);
        return response.data;
    } catch (error) {
        console.error('Error in getGoogleClientId:', error.response?.data || error.message);
        throw error;
    }
};

export const handleGoogleLogin = async (googleToken) => {
    try {

        const res = await apiClient.post('/auth/oauth/google', { token: googleToken.credential });
        localStorage.setItem('authToken', res.data.token);
        localStorage.setItem('userId', res.data.userId);
        console.log('User authenticated with Google:', res.data);
        return res.data;
    } catch (error) {
        console.error('Error during Google OAuth login:', error);
    }
};




export const authenticateUser = async (authData) => {
    try {
        const response = await apiClient.post('/auth/signin', authData);
        const { token, userId } = response.data;
        return { token, userId };
    } catch (error) {
        console.error('Error in authenticateUser:', error.response?.data || error.message);
        throw error;
    }
};

export const createGame = async (gameData) => {
    try {
        const response = await apiClient.post('/bees/game', gameData);
        return response.data;
    } catch (error) {
        console.error('Error in createGame:', error.response?.data || error.message);
        throw error;
    }
};

export const getGame = async (gameId) => {
    try {
        const response = await apiClient.get(`/bees/game/${gameId}`);
        return response.data;
    } catch (error) {
        console.error('Error getting data in BeesApiService:', error);
        throw error;
    }
};


export const iterateWeek = async (gameId, requestData) => {
    try {
        const response = await apiClient.post(`/bees/iterate/${gameId}`, requestData);

        console.log('token from iterateWeek', localStorage.getItem('authToken'))
        return response.data;
    } catch (error) {
        console.error('Error iterating week:', error);
        throw error;
    }
};


export const getHoneyQuantities = async (gameId) => {
    try {
        const response = await apiClient.get(`/bees/getHoneyQuantities/${gameId}`);
        return response.data;
    } catch (error) {
        console.error('Error sending honeyQuantities:', error);
        throw error;
    }
};




export const sendSellHoneyQuantities = {
    updateHoneyStock: async (gameId, honeyTypeToAmount, totalValue) => {
        try {
            const payload = {
                honeyTypeToAmount,
                totalValue,
            };
            console.log('Payload din BeesApiService:', JSON.stringify(payload, null, 2));
            const response = await apiClient.post(`/bees/sellHoney/${gameId}`, payload);
            return response.data;
        } catch (error) {
            console.error('Error sending SellHoneyQuantities:', error);
            throw error;
        }
    },
};



export const buyHives = async (gameId, numberOfHives) => {
    try {
        console.log("Number of hives to buy:", numberOfHives);
        const response = await apiClient.post(`/bees/buyHives/${gameId}`, { numberOfHives }, {
            headers: { 'Content-Type': 'application/json' }
        });
        return response.data;
    } catch (error) {
        console.error('Error buying hives:', error);
        throw error;
    }
};


export const fetchLocations = async (query) => {
    const apiKey = 'tiMHwUADw1sxyLnOfrbf2a6oyXhRHFBe';
    const url = `https://api.os.uk/search/names/v1/find?key=${apiKey}&query=${query}&fq=local_type:City`;

    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Error: ${response.statusText}`);
        }

        const data = await response.json();
        console.log(data);
        if (data.results && Array.isArray(data.results)) {
            return data.results.map(result => result.GAZETTEER_ENTRY.NAME1);
        } else {
            console.warn('No results found in the API response');
            return [];
        }
    } catch (error) {
        console.error('Failed to fetch locations:', error);
        return [];
    }
};
/*
export const fetchWeatherForStartDate = async (location) => {

    const apiUrl = `https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/${location}/2023-03-01/2023-09-30?unitGroup=metric&include=days&key=E62RRYZW29UJG5CTHCU2GE8MW&contentType=json&elements=temp,precip,windspeed`;

    try {
        const response = await fetch(apiUrl, {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error(`Error: ${response.statusText}`);
        }

        const data = await response.json();
        if (data.days && data.days.length > 0) {
            return data.days.map(day => ({
                date: day.datetime,
                temperature: day.temp,
                precipitation: day.precip,
                windSpeed: day.windspeed
            }));
        } else {
            console.warn('No weather data found for this date range.');
            return null;
        }
    } catch (error) {
        console.error('Failed to fetch weather data:', error);
        return null;
    }
};
*/


export const getPublicGames = async () => {
    try {
        const response = await apiClient.get('/bees/PublicGames', {
            params: { gameType: 'public' }
        });
        return response.data;
    } catch (error) {
        console.error('Error in getting public games:', error);
        throw error;
    }
};


export const getGamesForUserByType = async (userId, gameType) => {
    const response = await apiClient.get('/bees/gamesForUser', {
        params: {
            userId: userId,
            gameType: gameType
        }
    });
    return response.data;
};


export const getHiveHistory = async (gameId, hiveId) => {
    try {
        const response = await apiClient.get(
            `/bees/HiveHistory/${gameId}`,
            {
                params: { hiveId: hiveId }
            });
        return response.data;
    } catch (error) {
        console.error('Error getting data in getHiveHistory:', error);
        throw error;
    }
};


export const getApiaryHistory = async (gameId) => {
    try {
        const response = await apiClient.get(`/bees/apiaryHistory/${gameId}`);
        console.log('acesta e obiectul ApiaryHistory" ', response)
        if (!response || !response.data) {
            throw new Error('No data returned from the server');
        }
        return response.data;
    } catch (error) {
        console.error('Error getting data in getApiaryHistory:', error);
        throw new Error(
            error.response?.data?.message || 'Failed to fetch Apiary History'
        );
    }
};

export const deleteGame = async (gameId) => {
    try {
        const response = await apiClient.delete(`/bees/deleteGame/${gameId}`);
        console.log('This game will be deleted:', response);
        if (!response || !response.data) {
            throw new Error('No data returned from the server');
        }
        return response.data;
    } catch (error) {
        console.error('Error on delete game:', error.message);
        throw error;
    }
};

