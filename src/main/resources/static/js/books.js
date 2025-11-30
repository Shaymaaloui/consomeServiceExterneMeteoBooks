// Configuration de l'API
const API_BASE_URL = 'http://localhost:8081/api';

let allBooks = [];
let currentBookId = null;

// Charger les livres au chargement de la page
document.addEventListener('DOMContentLoaded', () => {
    loadBooks();
    
    // Ajouter l'écouteur pour la recherche
    document.getElementById('searchInput').addEventListener('input', filterBooks);
});

// Charger tous les livres
async function loadBooks() {
    const container = document.getElementById('booksContainer');
    
    try {
        const response = await fetch(`${API_BASE_URL}/books`);
        allBooks = await response.json();
        
        displayBooks(allBooks);
        
    } catch (error) {
        console.error('Erreur lors du chargement des livres:', error);
        container.innerHTML = `
            <div class="empty-state" style="grid-column: 1 / -1;">
                <i class="fas fa-exclamation-triangle"></i>
                <p>Erreur lors du chargement des livres</p>
                <p style="font-size: 0.9rem; margin-top: 0.5rem;">
                    Assurez-vous que l'API est démarrée sur le port 8081
                </p>
                <a href="http://localhost:8081/swagger-ui.html" target="_blank" class="btn btn-primary" style="margin-top: 1rem;">
                    <i class="fas fa-external-link-alt"></i> Vérifier l'API
                </a>
            </div>
        `;
    }
}

// Afficher les livres
function displayBooks(books) {
    const container = document.getElementById('booksContainer');
    
    if (books.length === 0) {
        container.innerHTML = `
            <div class="empty-state" style="grid-column: 1 / -1;">
                <i class="fas fa-book"></i>
                <p>Aucun livre trouvé</p>
                <button onclick="openAddBookModal()" class="btn btn-primary" style="margin-top: 1rem;">
                    <i class="fas fa-plus"></i> Ajouter le premier livre
                </button>
            </div>
        `;
        return;
    }
    
    container.innerHTML = books.map(book => `
        <div class="book-card">
            <h3>${escapeHtml(book.title)}</h3>
            <p class="book-author">
                <i class="fas fa-user"></i>
                ${escapeHtml(book.author)}
            </p>
            
            <div class="book-details">
                <div class="book-detail-item">
                    <span class="book-detail-label"><i class="fas fa-barcode"></i> ISBN</span>
                    <span class="book-detail-value">${escapeHtml(book.isbn)}</span>
                </div>
                ${book.publicationYear ? `
                    <div class="book-detail-item">
                        <span class="book-detail-label"><i class="fas fa-calendar"></i> Année</span>
                        <span class="book-detail-value">${book.publicationYear}</span>
                    </div>
                ` : ''}
            </div>
            
            ${book.description ? `
                <p style="margin-top: 1rem; color: #718096; font-size: 0.9rem; line-height: 1.5;">
                    ${escapeHtml(book.description.substring(0, 150))}${book.description.length > 150 ? '...' : ''}
                </p>
            ` : ''}
            
            <div class="book-price">${book.price.toFixed(2)} €</div>
            
            <div class="book-actions">
                <button onclick="editBook(${book.id})" class="btn btn-primary btn-small">
                    <i class="fas fa-edit"></i> Modifier
                </button>
                <button onclick="deleteBook(${book.id}, '${escapeHtml(book.title).replace(/'/g, "\\\'")}')" class="btn btn-danger btn-small">
                    <i class="fas fa-trash"></i> Supprimer
                </button>
            </div>
        </div>
    `).join('');
}

// Filtrer les livres
function filterBooks() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    
    const filteredBooks = allBooks.filter(book => 
        book.title.toLowerCase().includes(searchTerm) ||
        book.author.toLowerCase().includes(searchTerm) ||
        book.isbn.toLowerCase().includes(searchTerm)
    );
    
    displayBooks(filteredBooks);
}

// Ouvrir le modal d'ajout
function openAddBookModal() {
    document.getElementById('modalTitle').innerHTML = '<i class="fas fa-plus"></i> Ajouter un Livre';
    document.getElementById('bookForm').reset();
    document.getElementById('bookId').value = '';
    document.getElementById('bookModal').classList.add('active');
}

// Fermer le modal
function closeBookModal() {
    document.getElementById('bookModal').classList.remove('active');
}

// Modifier un livre
function editBook(id) {
    const book = allBooks.find(b => b.id === id);
    if (!book) return;
    
    document.getElementById('modalTitle').innerHTML = '<i class="fas fa-edit"></i> Modifier un Livre';
    document.getElementById('bookId').value = book.id;
    document.getElementById('title').value = book.title;
    document.getElementById('author').value = book.author;
    document.getElementById('isbn').value = book.isbn;
    document.getElementById('price').value = book.price;
    document.getElementById('publicationYear').value = book.publicationYear || '';
    document.getElementById('description').value = book.description || '';
    
    document.getElementById('bookModal').classList.add('active');
}

// Sauvegarder un livre (créer ou modifier)
async function saveBook(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const bookId = formData.get('id');
    
    const bookData = {
        title: formData.get('title'),
        author: formData.get('author'),
        isbn: formData.get('isbn'),
        price: parseFloat(formData.get('price')),
        publicationYear: formData.get('publicationYear') ? parseInt(formData.get('publicationYear')) : null,
        description: formData.get('description') || null
    };
    
    try {
        const url = bookId ? `${API_BASE_URL}/books/${bookId}` : `${API_BASE_URL}/books`;
        const method = bookId ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bookData)
        });
        
        if (response.ok) {
            showToast(bookId ? 'Livre modifié avec succès' : 'Livre ajouté avec succès', 'success');
            closeBookModal();
            loadBooks();
        } else {
            const error = await response.text();
            showToast('Erreur: ' + error, 'error');
        }
        
    } catch (error) {
        console.error('Erreur lors de la sauvegarde:', error);
        showToast('Erreur lors de la sauvegarde du livre', 'error');
    }
}

// Supprimer un livre
function deleteBook(id, title) {
    currentBookId = id;
    document.getElementById('deleteBookTitle').textContent = title;
    document.getElementById('deleteModal').classList.add('active');
}

// Confirmer la suppression
async function confirmDelete() {
    try {
        const response = await fetch(`${API_BASE_URL}/books/${currentBookId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showToast('Livre supprimé avec succès', 'success');
            closeDeleteModal();
            loadBooks();
        } else {
            showToast('Erreur lors de la suppression', 'error');
        }
        
    } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        showToast('Erreur lors de la suppression du livre', 'error');
    }
}

// Fermer le modal de suppression
function closeDeleteModal() {
    document.getElementById('deleteModal').classList.remove('active');
    currentBookId = null;
}

// Afficher une notification toast
function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type} show`;
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// Fonction utilitaire pour échapper le HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Fermer les modals en cliquant à l'extérieur
window.addEventListener('click', (event) => {
    const bookModal = document.getElementById('bookModal');
    const deleteModal = document.getElementById('deleteModal');
    
    if (event.target === bookModal) {
        closeBookModal();
    }
    if (event.target === deleteModal) {
        closeDeleteModal();
    }
});
