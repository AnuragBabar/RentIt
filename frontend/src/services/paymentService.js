import api from '../api/axios';

const PaymentService = {
    makePayment: async (paymentData) => {
        // paymentData should be { bookingId, amount }
        const response = await api.post('/payments', paymentData);
        return response.data;
    }
};

export default PaymentService;
