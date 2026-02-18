import React, { useState, useEffect } from 'react';
import BookingService from '../services/bookingService';
import PaymentService from '../services/paymentService';
import { useAuth } from '../context/AuthContext';
import { formatCurrency } from '../utils/formatters';
import { useItems } from '../hooks/useItems';
import { Package, Calendar, Clock, CheckCircle, XCircle, Search } from 'lucide-react';

const RenterDashboard = () => {
    const { items, loading: itemsLoading, error: itemsError } = useItems();
    const [bookings, setBookings] = useState([]);
    const [activeTab, setActiveTab] = useState('browse');
    const [bookingDate, setBookingDate] = useState({ start: '', end: '' });
    const [selectedItem, setSelectedItem] = useState(null);
    const { user } = useAuth();

    useEffect(() => {
        // fetchItems() is no longer needed as useItems handles it
        fetchMyBookings();
    }, []);

    const fetchMyBookings = async () => {
        try {
            const data = await BookingService.getBookingsByRenter(user.userId);
            setBookings(data);
        } catch (err) {
            console.error("Failed to fetch bookings", err);
        }
    };

    const handleBooking = async (e) => {
        e.preventDefault();
        try {
            await BookingService.createBooking({
                itemId: selectedItem.id,
                renterId: user.userId,
                ownerId: selectedItem.ownerId,
                startDate: bookingDate.start,
                endDate: bookingDate.end
            });
            alert("Booking request sent!");
            setSelectedItem(null);
            fetchMyBookings();
        } catch (err) {
            alert("Booking failed: " + (err.response?.data?.message || "Check dates"));
        }
    };

    const handleCancelBooking = async (bookingId) => {
        if (!window.confirm("Are you sure you want to cancel this booking?")) return;
        try {
            await BookingService.cancelBooking(bookingId);
            fetchMyBookings();
        } catch (err) {
            alert("Failed to cancel booking");
        }
    };

    const calculateTotal = (booking) => {
        const days = Math.max(1, (new Date(booking.endDate) - new Date(booking.startDate)) / (1000 * 60 * 60 * 24));
        return booking.pricePerDay * days;
    };

    const handleConfirmPayment = async (bookingId) => {
        const booking = bookings.find(b => b.id === bookingId);
        if (!booking) return;

        const amount = calculateTotal(booking);

        if (!window.confirm(`Proceed with payment of Rs. ${amount}?`)) return;

        try {
            // 1. Process Payment
            await PaymentService.makePayment({
                bookingId: bookingId,
                amount: amount
            });

            // 2. If payment successful, confirm booking
            await BookingService.confirmBooking(bookingId);

            alert("Payment successful! Booking confirmed.");
            fetchMyBookings();
        } catch (err) {
            console.error(err);
            const errorMsg = err.response?.data?.message || err.message;
            alert("Payment failed: " + errorMsg);
        }
    };

    const getStatusStyle = (status) => {
        switch (status) {
            case 'APPROVED': return { color: 'var(--success)', background: 'rgba(16, 185, 129, 0.1)' };
            case 'REJECTED': return { color: 'var(--error)', background: 'rgba(239, 68, 68, 0.1)' };
            case 'CONFIRMED': return { color: '#fbbf24', background: 'rgba(251, 191, 36, 0.1)' };
            default: return { color: 'var(--text-muted)', background: 'var(--glass)' };
        }
    };

    return (
        <div className="container">
            <div style={{ display: 'flex', gap: '2rem', marginBottom: '3rem' }}>
                <button
                    className={activeTab === 'browse' ? 'btn-primary' : 'btn-outline'}
                    onClick={() => setActiveTab('browse')}
                    style={{ padding: '0.5rem 2rem' }}
                >
                    Browse items
                </button>
                <button
                    className={activeTab === 'bookings' ? 'btn-primary' : 'btn-outline'}
                    onClick={() => setActiveTab('bookings')}
                    style={{ padding: '1rem 2rem' }}
                >
                    My Bookings
                </button>
            </div>

            {activeTab === 'browse' ? (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '2rem' }}>
                    {itemsLoading ? (
                        <p>Loading items...</p>
                    ) : items.length === 0 ? (
                        <p>No items available for rent right now.</p>
                    ) : items.map(item => (
                        <div key={item.id} className="glass-card animate-in" style={{ padding: '1.5rem' }}>
                            <div style={{ height: '200px', background: 'var(--glass)', borderRadius: '16px', marginBottom: '1rem', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                                {item.imageUrl ? (
                                    <img src={item.imageUrl.startsWith('http') ? item.imageUrl : `http://localhost:8080${item.imageUrl}`} alt={item.itemName} style={{ width: '100%', height: '100%', objectFit: 'cover', borderRadius: '16px' }} />
                                ) : (
                                    <Package size={48} color="var(--text-muted)" />
                                )}
                            </div>
                            <h3>{item.itemName}</h3>
                            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', margin: '0.5rem 0' }}>{item.description}</p>
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '1.5rem' }}>
                                <span style={{ fontSize: '1.25rem', fontWeight: '700', color: 'var(--primary)' }}>{formatCurrency(item.pricePerDay)}<small style={{ fontSize: '0.8rem', fontWeight: '400', color: 'var(--text-muted)' }}>/day</small></span>
                                <button className="btn-primary" onClick={() => setSelectedItem(item)}>Rent Now</button>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className="glass-card">
                    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                        <thead>
                            <tr style={{ textAlign: 'left', color: 'var(--text-muted)', fontSize: '0.9rem' }}>
                                <th style={{ padding: '1rem' }}>Item</th>
                                <th style={{ padding: '1rem' }}>Owner</th>
                                <th style={{ padding: '1rem' }}>Dates</th>
                                <th style={{ padding: '1rem' }}>Total Price</th>
                                <th style={{ padding: '1rem' }}>Status</th>
                                <th style={{ padding: '1rem' }}>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {bookings.map(booking => (
                                <tr key={booking.id} style={{ borderTop: '1px solid var(--glass-border)' }}>
                                    <td style={{ padding: '1.5rem 1rem' }}>
                                        <div style={{ fontWeight: '600' }}>{booking.itemName || `ID: ${booking.itemId}`}</div>
                                        <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Booking #{booking.id}</div>
                                    </td>
                                    <td style={{ padding: '1.5rem 1rem' }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                            <div style={{ width: '24px', height: '24px', borderRadius: '50%', background: 'var(--primary)', color: 'white', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.75rem' }}>
                                                {booking.ownerName ? booking.ownerName.charAt(0).toUpperCase() : '?'}
                                            </div>
                                            {booking.ownerName || 'Unknown'}
                                        </div>
                                    </td>
                                    <td style={{ padding: '1.5rem 1rem' }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.9rem' }}>
                                            <Calendar size={14} /> {booking.startDate} to {booking.endDate}
                                        </div>
                                    </td>
                                    <td style={{ padding: '1.5rem 1rem' }}>{formatCurrency(booking.pricePerDay * (Math.max(1, (new Date(booking.endDate) - new Date(booking.startDate)) / (1000 * 60 * 60 * 24))))}</td>
                                    <td style={{ padding: '1.5rem 1rem' }}>
                                        <span style={{ padding: '0.5rem 1rem', borderRadius: '20px', fontSize: '0.8rem', fontWeight: '600', ...getStatusStyle(booking.status) }}>
                                            {booking.status}
                                        </span>
                                    </td>
                                    <td style={{ padding: '1.5rem 1rem' }}>
                                        {booking.status === 'REQUESTED' && (
                                            <button
                                                className="btn-outline"
                                                style={{ padding: '0.4rem 0.8rem', fontSize: '0.8rem', color: 'var(--error)', borderColor: 'var(--error)' }}
                                                onClick={() => handleCancelBooking(booking.id)}
                                            >
                                                Cancel
                                            </button>
                                        )}
                                        {booking.status === 'APPROVED' && (
                                            <button
                                                className="btn-primary"
                                                style={{ padding: '0.4rem 1rem', fontSize: '0.8rem' }}
                                                onClick={() => handleConfirmPayment(booking.id)}
                                            >
                                                Pay Now
                                            </button>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}

            {selectedItem && (
                <div style={{ position: 'fixed', top: 0, left: 0, width: '100%', height: '100%', background: 'rgba(0,0,0,0.8)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 2000 }}>
                    <div className="glass-card" style={{ width: '100%', maxWidth: '500px' }}>
                        <h3>Rent {selectedItem.itemName}</h3>
                        <form onSubmit={handleBooking} style={{ marginTop: '2rem' }}>
                            <div className="input-group">
                                <label>Start Date</label>
                                <input type="date" value={bookingDate.start} onChange={e => setBookingDate({ ...bookingDate, start: e.target.value })} required />
                            </div>
                            <div className="input-group">
                                <label>End Date</label>
                                <input type="date" value={bookingDate.end} onChange={e => setBookingDate({ ...bookingDate, end: e.target.value })} required />
                            </div>
                            <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
                                <button type="button" className="btn-outline" style={{ flex: 1 }} onClick={() => setSelectedItem(null)}>Cancel</button>
                                <button type="submit" className="btn-primary" style={{ flex: 1 }}>Confirm Booking</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default RenterDashboard;
