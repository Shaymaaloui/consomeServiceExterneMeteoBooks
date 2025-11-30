// Configuration de l'API
const API_BASE_URL = 'http://localhost:8081/api';

let currentCity = 'tunis';

// Charger la météo au chargement de la page
document.addEventListener('DOMContentLoaded', () => {
    // Activer le bouton Tunis par défaut
    const tunisBtn = document.querySelector('[onclick*="tunis"]');
    if (tunisBtn) {
        tunisBtn.classList.add('active');
    }
    loadWeather('tunis');
});

// Charger la météo pour une ville
async function loadWeather(city, clickEvent) {
    currentCity = city;
    const container = document.getElementById('weatherContainer');
    
    // Mettre à jour les boutons actifs
    document.querySelectorAll('.city-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Activer le bouton cliqué (si event existe)
    if (clickEvent && clickEvent.target) {
        clickEvent.target.classList.add('active');
    }
    
    // Cacher le formulaire personnalisé
    const customForm = document.getElementById('customLocationForm');
    if (customForm) {
        customForm.style.display = 'none';
    }
    
    container.innerHTML = `
        <div class="loading">
            <i class="fas fa-spinner fa-spin"></i>
            <p>Chargement de la météo...</p>
        </div>
    `;
    
    try {
        console.log(`Chargement de la météo pour: ${city}`);
        const response = await fetch(`${API_BASE_URL}/weather/${city}`);
        
        if (!response.ok) {
            throw new Error(`Erreur HTTP: ${response.status}`);
        }
        
        const data = await response.json();
        console.log(`Données reçues pour ${city}:`, data);
        
        displayWeather(data);
        
    } catch (error) {
        console.error('Erreur lors du chargement de la météo:', error);
        container.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-exclamation-triangle"></i>
                <p>Erreur lors du chargement de la météo</p>
                <p style="font-size: 0.9rem; margin-top: 0.5rem;">
                    ${error.message || 'Assurez-vous que l\'API est démarrée sur le port 8081'}
                </p>
            </div>
        `;
    }
}

// Charger la météo pour une localisation personnalisée
async function loadCustomWeather() {
    const latitude = parseFloat(document.getElementById('latitude').value);
    const longitude = parseFloat(document.getElementById('longitude').value);
    
    if (isNaN(latitude) || isNaN(longitude)) {
        alert('Veuillez entrer des coordonnées valides');
        return;
    }
    
    const container = document.getElementById('weatherContainer');
    
    // Désactiver tous les boutons de ville
    document.querySelectorAll('.city-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    container.innerHTML = `
        <div class="loading">
            <i class="fas fa-spinner fa-spin"></i>
            <p>Chargement de la météo...</p>
        </div>
    `;
    
    try {
        const response = await fetch(`${API_BASE_URL}/weather?latitude=${latitude}&longitude=${longitude}`);
        const data = await response.json();
        
        // L'API retourne maintenant un format cohérent avec currentWeather
        displayWeather(data);
        
    } catch (error) {
        console.error('Erreur lors du chargement de la météo:', error);
        container.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-exclamation-triangle"></i>
                <p>Erreur lors du chargement de la météo</p>
                <p style="font-size: 0.9rem; margin-top: 0.5rem;">
                    Vérifiez les coordonnées et réessayez
                </p>
            </div>
        `;
    }
}

// Afficher les données météo
function displayWeather(data) {
    const container = document.getElementById('weatherContainer');
    
    console.log('displayWeather reçoit:', data);
    console.log('currentWeather:', data.currentWeather);
    
    if (!data.currentWeather) {
        console.error('currentWeather est manquant!');
        container.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-cloud"></i>
                <p>Données météo non disponibles</p>
            </div>
        `;
        return;
    }
    
    const weather = data.currentWeather;
    console.log('weather object:', weather);
    console.log('temperature:', weather.temperature);
    console.log('windSpeed:', weather.windSpeed);
    console.log('weathercode:', weather.weathercode);
    
    const temperature = weather.temperature ? weather.temperature.replace('°C', '') : 'N/A';
    const windSpeed = weather.windSpeed ? weather.windSpeed.replace(' km/h', '') : 'N/A';
    const time = weather.time ? new Date(weather.time).toLocaleString('fr-FR') : new Date().toLocaleString('fr-FR');
    
    container.innerHTML = `
        <div class="weather-card">
            <div class="weather-header">
                <div>
                    <h2 class="weather-city">
                        <i class="fas fa-map-marker-alt"></i>
                        ${escapeHtml(data.city)}
                    </h2>
                    <p class="weather-time">
                        <i class="fas fa-clock"></i>
                        ${time}
                    </p>
                </div>
                <div style="text-align: right;">
                    <p style="color: #718096; font-size: 0.9rem;">Coordonnées</p>
                    <p style="font-weight: 500;">
                        ${data.latitude.toFixed(4)}° N, ${data.longitude.toFixed(4)}° E
                    </p>
                </div>
            </div>
            
            <div class="weather-main">
                <div class="weather-temp">
                    <div class="temp-value">${temperature}°</div>
                    <p class="weather-description">
                        ${getWeatherIcon(weather.weathercode)}
                        ${escapeHtml(weather.description || 'N/A')}
                    </p>
                </div>
                
                <div class="weather-details">
                    <div class="weather-detail-item">
                        <div class="weather-detail-icon">
                            <i class="fas fa-wind"></i>
                        </div>
                        <div class="weather-detail-info">
                            <h4>Vitesse du vent</h4>
                            <p>${windSpeed} km/h</p>
                        </div>
                    </div>
                    
                    ${weather.weathercode !== undefined ? `
                        <div class="weather-detail-item">
                            <div class="weather-detail-icon">
                                <i class="fas fa-cloud"></i>
                            </div>
                            <div class="weather-detail-info">
                                <h4>Code météo</h4>
                                <p>${weather.weathercode}</p>
                            </div>
                        </div>
                    ` : ''}
                </div>
            </div>
            
            <div style="margin-top: 2rem; padding: 1.5rem; background: var(--light-color); border-radius: 8px;">
                <p style="color: #718096; font-size: 0.9rem; text-align: center;">
                    <i class="fas fa-info-circle"></i>
                    Données fournies par l'API Open-Meteo (service externe gratuit)
                </p>
            </div>
        </div>
    `;
}

// Afficher le formulaire de localisation personnalisée
function showCustomLocation(clickEvent) {
    document.querySelectorAll('.city-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    if (clickEvent && clickEvent.target) {
        clickEvent.target.classList.add('active');
    }
    
    const form = document.getElementById('customLocationForm');
    if (form) {
        form.style.display = form.style.display === 'none' ? 'block' : 'none';
    }
}

// Obtenir la description météo selon le code
function getWeatherDescription(code) {
    if (code === null || code === undefined) return 'Inconnu';
    
    const descriptions = {
        0: 'Ciel dégagé',
        1: 'Partiellement nuageux',
        2: 'Partiellement nuageux',
        3: 'Partiellement nuageux',
        45: 'Brouillard',
        48: 'Brouillard',
        51: 'Bruine légère',
        53: 'Bruine modérée',
        55: 'Bruine dense',
        61: 'Pluie légère',
        63: 'Pluie modérée',
        65: 'Pluie forte',
        71: 'Neige légère',
        73: 'Neige modérée',
        75: 'Neige forte',
        80: 'Averses légères',
        81: 'Averses modérées',
        82: 'Averses violentes',
        95: 'Orage',
        96: 'Orage avec grêle légère',
        99: 'Orage avec grêle forte'
    };
    
    return descriptions[code] || 'Conditions météo variées';
}

// Obtenir l'icône météo selon le code
function getWeatherIcon(code) {
    if (code === null || code === undefined) return '<i class="fas fa-question"></i>';
    
    const icons = {
        0: '<i class="fas fa-sun"></i>',
        1: '<i class="fas fa-cloud-sun"></i>',
        2: '<i class="fas fa-cloud-sun"></i>',
        3: '<i class="fas fa-cloud"></i>',
        45: '<i class="fas fa-smog"></i>',
        48: '<i class="fas fa-smog"></i>',
        51: '<i class="fas fa-cloud-rain"></i>',
        53: '<i class="fas fa-cloud-rain"></i>',
        55: '<i class="fas fa-cloud-showers-heavy"></i>',
        61: '<i class="fas fa-cloud-rain"></i>',
        63: '<i class="fas fa-cloud-rain"></i>',
        65: '<i class="fas fa-cloud-showers-heavy"></i>',
        71: '<i class="fas fa-snowflake"></i>',
        73: '<i class="fas fa-snowflake"></i>',
        75: '<i class="fas fa-snowflake"></i>',
        80: '<i class="fas fa-cloud-showers-heavy"></i>',
        81: '<i class="fas fa-cloud-showers-heavy"></i>',
        82: '<i class="fas fa-cloud-showers-heavy"></i>',
        95: '<i class="fas fa-bolt"></i>',
        96: '<i class="fas fa-bolt"></i>',
        99: '<i class="fas fa-bolt"></i>'
    };
    
    return icons[code] || '<i class="fas fa-cloud"></i>';
}

// Fonction utilitaire pour échapper le HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
