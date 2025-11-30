// Configuration de l'API
const API_BASE_URL = 'http://localhost:8081/api';

// Charger les données au chargement de la page
document.addEventListener('DOMContentLoaded', () => {
    loadDashboardData();
    loadRecentBooks();
});

// Charger les données du tableau de bord
async function loadDashboardData() {
    try {
        // Charger les statistiques des livres
        const booksResponse = await fetch(`${API_BASE_URL}/books`);
        const books = await booksResponse.json();
        
        // Mettre à jour le nombre total de livres
        document.getElementById('totalBooks').textContent = books.length;
        
        // Calculer le nombre d'auteurs uniques
        const uniqueAuthors = new Set(books.map(book => book.author)).size;
        document.getElementById('totalAuthors').textContent = uniqueAuthors;
        
        // Charger la météo pour Tunis
        const weatherResponse = await fetch(`${API_BASE_URL}/weather/tunis`);
        const weather = await weatherResponse.json();
        
        if (weather.currentWeather && weather.currentWeather.temperature) {
            document.getElementById('temperature').textContent = weather.currentWeather.temperature;
        } else {
            document.getElementById('temperature').textContent = 'N/A';
        }
        
        // Mettre à jour le statut de l'API
        document.getElementById('apiStatus').innerHTML = '<i class="fas fa-check-circle"></i>';
        
    } catch (error) {
        console.error('Erreur lors du chargement des données:', error);
        document.getElementById('apiStatus').innerHTML = '<i class="fas fa-times-circle"></i>';
        document.getElementById('apiStatus').parentElement.querySelector('p').textContent = 'API Indisponible';
    }
}

// Charger les livres récents
async function loadRecentBooks() {
    const container = document.getElementById('recentBooksList');
    
    try {
        const response = await fetch(`${API_BASE_URL}/books`);
        const books = await response.json();
        
        if (books.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-book"></i>
                    <p>Aucun livre disponible</p>
                </div>
            `;
            return;
        }
        
        // Afficher les 3 premiers livres
        const recentBooks = books.slice(0, 3);
        
        container.innerHTML = recentBooks.map(book => `
            <div class="book-card">
                <h3>${escapeHtml(book.title)}</h3>
                <p class="book-author">
                    <i class="fas fa-user"></i>
                    ${escapeHtml(book.author)}
                </p>
                <div class="book-details">
                    <div class="book-detail-item">
                        <span class="book-detail-label">ISBN</span>
                        <span class="book-detail-value">${escapeHtml(book.isbn)}</span>
                    </div>
                    ${book.publicationYear ? `
                        <div class="book-detail-item">
                            <span class="book-detail-label">Année</span>
                            <span class="book-detail-value">${book.publicationYear}</span>
                        </div>
                    ` : ''}
                </div>
                ${book.description ? `
                    <p style="margin-top: 1rem; color: #718096; font-size: 0.9rem;">
                        ${escapeHtml(book.description.substring(0, 100))}${book.description.length > 100 ? '...' : ''}
                    </p>
                ` : ''}
                <div class="book-price">${book.price.toFixed(2)} €</div>
                <a href="books.html" class="btn btn-primary btn-small" style="width: 100%; justify-content: center;">
                    <i class="fas fa-eye"></i> Voir détails
                </a>
            </div>
        `).join('');
        
    } catch (error) {
        console.error('Erreur lors du chargement des livres:', error);
        container.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-exclamation-triangle"></i>
                <p>Erreur lors du chargement des livres</p>
                <p style="font-size: 0.9rem; margin-top: 0.5rem;">
                    Assurez-vous que l'API est démarrée sur le port 8081
                </p>
            </div>
        `;
    }
}

// Fonction utilitaire pour échapper le HTML (sécurité XSS)
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
