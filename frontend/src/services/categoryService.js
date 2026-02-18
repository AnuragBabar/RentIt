import api from '../api/axios';

const CategoryService = {
    getAllCategories: async () => {
        const response = await api.get('/categories');
        return response.data;
    },

    createCategory: async (categoryData) => {
        const response = await api.post('/categories', categoryData);
        return response.data;
    }
};

export default CategoryService;
