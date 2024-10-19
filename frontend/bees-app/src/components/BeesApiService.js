import axios from 'axios';

export const createGame = async (gameData) => {
    try {
        const response = await axios.post('http://localhost:8080/api/bees/game', gameData);
        return response.data;
    } catch (error) {
        console.error('Error fetching game data:', error);
        throw error;
    }
};

export const getGame = async () => {
    try {
        const response = await axios.get('http://localhost:8080/api/bees/game/0');
        return response.data;
    } catch (error) {
        console.error('Error fetching game data:', error);
        throw error;
    }
};

export const iterateWeek = async () => {
    try {
        const response = await axios.post('http://localhost:8080/api/bees/iterate/0');
        if (response.status !== 200) {
            throw new Error('Failed to iterate week, status: ' + response.status);
        }
        return response.data;
    } catch (error) {
        console.error('Error iterating week:', error);
        throw error;
    }
};

export const submitActionsOfTheWeek = async (actionsData) => {
    try {
        const response = await axios.post('http://localhost:8080/api/bees/submitActionsOfTheWeek/0', actionsData);
        if (response.status !== 200) {
            throw new Error('Failed to submit actions, status: ' + response.status);
        }
        return response.data;
    } catch (error) {
        console.error('Error submitting actions:', error);
        throw error;
    }
};




export const getHoneyQuantities = async () => {
    const response = await fetch('http://localhost:8080/api/bees/honey-quantities', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });
    if (!response.ok) {
        throw new Error('Failed to fetch honey quantities');
    }
    return await response.json();
};

export const getGameInfos = [
    {
        gameName: "Stefan Cel Mare Apiary",
        location: "Suceava, Romania",
        hives: "50000000000000",
        bees: "23456789",
        honey: "76677"
    },
    {
        gameName: "Vlad Tepes Apiary",
        location: "Targoviste, Romania",
        hives: "30000000000000",
        bees: "12345678",
        honey: "56677"
    },
    {
        gameName: "Mihai Viteazul Apiary",
        location: "Alba Iulia, Romania",
        hives: "20000000000000",
        bees: "34567890",
        honey: "46677"
    },
    {
        gameName: "Stefan Cel Mititel Apiary",
        location: "Suceava, Romania",
        hives: "50000000000000",
        bees: "23456789",
        honey: "76677"
    },
    {
        gameName: "Vlad Impaler Apiary",
        location: "Targoviste, Romania",
        hives: "30000000000000",
        bees: "12345678",
        honey: "56677"
    },
    {
        gameName: "Mihai Cel Fricos Apiary",
        location: "Alba Iulia, Romania",
        hives: "20000000000000",
        bees: "34567890",
        honey: "46677"
    },
    {
        gameName: "Mihai Cel Fricos Apiary",
        location: "Alba Iulia, Romania",
        hives: "20000000000000",
        bees: "34567890",
        honey: "46677"
    }
];
