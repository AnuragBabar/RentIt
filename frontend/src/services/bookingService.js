import api from '../api/axios';

const BookingService = {
    createBooking: async (bookingData) => {
        const response = await api.post('/bookings', bookingData);
        return response.data;
    },

    getBookingsByRenter: async (renterId) => {
        const response = await api.get(`/bookings/renter/${renterId}`);
        return response.data;
    },

    getBookingsByOwner: async (ownerId) => {
        const response = await api.get(`/bookings/owner/${ownerId}`);
        return response.data;
    },

    approveBooking: async (bookingId) => {
        const response = await api.put(`/bookings/${bookingId}/approve`);
        return response.data;
    },

    rejectBooking: async (bookingId) => {
        const response = await api.put(`/bookings/${bookingId}/reject`);
        return response.data;
    },

    cancelBooking: async (bookingId) => {
        const response = await api.put(`/bookings/${bookingId}/cancel`);
        return response.data;
    },

    confirmBooking: async (bookingId) => {
        const response = await api.put(`/bookings/${bookingId}/confirm`);
        return response.data;
    }
};

export default BookingService;
