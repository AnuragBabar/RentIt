import api from '../api/axios';

const ItemService = {
    getAvailableItems: async () => {
        const response = await api.get('/items/available');
        return response.data;
    },

    getItemsByOwner: async (ownerId) => {
        const response = await api.get(`/items/owner/${ownerId}`);
        return response.data;
    },

    createItem: async (itemData) => {
        const response = await api.post('/items/addItem', itemData);
        return response.data;
    },

    updateItem: async (id, itemData) => {
        const response = await api.put(`/items/${id}`, itemData);
        return response.data;
    },

    deleteItem: async (id) => {
        const response = await api.delete(`/items/${id}`);
        return response.data;
    },

    uploadImage: async (file) => {
        const formData = new FormData();
        formData.append('file', file);
        const response = await api.post('/items/upload', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
        return response.data;
    }
};

export default ItemService;
