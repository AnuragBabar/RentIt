import React, { useState, useEffect } from 'react';
import ItemService from '../services/itemService';
import CategoryService from '../services/categoryService';
import BookingService from '../services/bookingService';
import { useAuth } from '../context/AuthContext';
import { Plus, Package, List, Check, X, Upload, Trash2, Edit } from 'lucide-react';

const OwnerDashboard = () => {
    const [items, setItems] = useState([]);
    const [categories, setCategories] = useState([]);
    const [bookings, setBookings] = useState([]);
    const [activeTab, setActiveTab] = useState('items');
    const [showItemModal, setShowItemModal] = useState(false);
    const [newItem, setNewItem] = useState({ itemName: '', description: '', pricePerDay: '', categoryName: '', imageFile: null });
    const [isEditing, setIsEditing] = useState(false);
    const [editingItemId, setEditingItemId] = useState(null);
    const { user } = useAuth();

    useEffect(() => {
        if (user?.userId) {
            fetchOwnerData();
        }
    }, [user]);

    const fetchOwnerData = async () => {
        try {
            const itemsData = await ItemService.getItemsByOwner(user.userId).catch(err => { console.error("Items fetch failed", err); return []; });
            const catsData = await CategoryService.getAllCategories().catch(err => { console.error("Cats fetch failed", err); return []; });
            const bookingsData = await BookingService.getBookingsByOwner(user.userId).catch(err => { console.error("Bookings fetch failed", err); return []; });

            console.log("Current user ID:", user?.userId);
            setItems(itemsData);
            setCategories(catsData);
            setBookings(bookingsData);
        } catch (err) {
            console.error("Fetch failed", err);
        }
    };

    const handleAddItem = async (e) => {
        e.preventDefault();
        try {
            let uploadedImageUrl = '';

            // 1. Upload image if selected
            if (newItem.imageFile) {
                uploadedImageUrl = await ItemService.uploadImage(newItem.imageFile);
            }

            let finalCategoryId;
            // 2. Check if category exists
            const existingCat = categories.find(c => c.name.toLowerCase() === (newItem.categoryName || '').toLowerCase());

            if (existingCat) {
                finalCategoryId = existingCat.id;
            } else {
                // 3. Create new category
                const newCat = await CategoryService.createCategory({ name: newItem.categoryName });
                finalCategoryId = newCat.id;
                setCategories([...categories, newCat]);
            }

            // 4. Create or Update Item
            const payload = {
                itemName: newItem.itemName,
                description: newItem.description,
                pricePerDay: parseFloat(newItem.pricePerDay),
                categoryId: finalCategoryId,
                ownerId: user.userId,
                imageUrl: uploadedImageUrl || newItem.existingImageUrl // Use existing if no new upload
            };

            if (isEditing) {
                await ItemService.updateItem(editingItemId, payload);
            } else {
                await ItemService.createItem(payload);
            }

            closeModal();
            fetchOwnerData();
        } catch (err) {
            console.error("Error adding item:", err);
            const errorMsg = err.response?.data?.message || err.response?.data || err.message || "Failed to add item";
            alert("Error: " + errorMsg);
        }
    };

    const handleDeleteItem = async (id) => {
        if (!window.confirm("Are you sure you want to delete this item?")) return;
        try {
            await ItemService.deleteItem(id);
            fetchOwnerData();
        } catch (err) {
            alert("Failed to delete item");
        }
    };

    const handleEditItem = (item) => {
        setNewItem({
            itemName: item.itemName,
            description: item.description,
            pricePerDay: item.pricePerDay,
            categoryName: item.categoryName || '', // categoryName might not be populated in item response, assuming it might be or handled
            // NOTE: If categoryName is not in item response, this might default to empty. 
            // Ideally backend returns category name or we find it from categories list.
            imageFile: null,
            existingImageUrl: item.imageUrl
        });

        // Try to find category name from ID if not present
        if (!item.categoryName && item.categoryId) {
            const cat = categories.find(c => c.id === item.categoryId);
            if (cat) {
                setNewItem(prev => ({ ...prev, categoryName: cat.name }));
            }
        }

        setEditingItemId(item.id);
        setIsEditing(true);
        setShowItemModal(true);
    };

    const closeModal = () => {
        setShowItemModal(false);
        setNewItem({ itemName: '', description: '', pricePerDay: '', categoryName: '', imageFile: null });
        setIsEditing(false);
        setEditingItemId(null);
    };

    const handleBookingAction = async (id, action) => {
        try {
            if (action === 'approve') {
                await BookingService.approveBooking(id);
            } else if (action === 'reject') {
                await BookingService.rejectBooking(id);
            }
            fetchOwnerData();
        } catch (err) {
            const errorMsg = err.response?.data?.message || err.message || "Unknown error";
            alert(`Failed to ${action} booking: ${errorMsg}`);
        }
    };

    return (
        <div className="container">

            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '3rem' }}>
                <div style={{ display: 'flex', gap: '1rem' }}>
                    <button className={activeTab === 'items' ? 'btn-primary' : 'btn-outline'} onClick={() => setActiveTab('items')}>My Items</button>
                    <button className={activeTab === 'requests' ? 'btn-primary' : 'btn-outline'} onClick={() => setActiveTab('requests')}>Booking Requests</button>
                </div>
                <button className="btn-primary" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }} onClick={() => { setIsEditing(false); setShowItemModal(true); }}>
                    <Plus size={20} /> Add New Item
                </button>
            </div>

            {activeTab === 'items' ? (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '2rem' }}>
                    {(!items || items.length === 0) ? (
                        <div className="glass-card" style={{ padding: '2rem', textAlign: 'center', gridColumn: '1 / -1' }}>
                            <Package size={48} color="var(--text-muted)" style={{ marginBottom: '1rem' }} />
                            <h3>No items listed yet</h3>
                            <p style={{ color: 'var(--text-muted)' }}>Click "Add New Item" to start your rental business.</p>
                        </div>
                    ) : (
                        items.map(item => (
                            <div key={item.id} className="glass-card animate-in" style={{ padding: '1rem' }}>
                                <div style={{ height: '160px', background: 'var(--glass)', borderRadius: '12px', marginBottom: '1rem', overflow: 'hidden' }}>
                                    {item.imageUrl ? (
                                        <img src={item.imageUrl.startsWith('http') ? item.imageUrl : `http://localhost:8080${item.imageUrl}`} alt="" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                                    ) : (
                                        <div style={{ height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Package size={40} color="var(--text-muted)" /></div>
                                    )}
                                </div>
                                <h4>{item.itemName}</h4>
                                <p style={{ color: 'var(--text-muted)', fontSize: '0.8rem', margin: '0.5rem 0' }}>{item.description}</p>
                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '1rem' }}>
                                    <span style={{ fontWeight: '700' }}>Rs. {item.pricePerDay}/day</span>
                                    <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'center' }}>
                                        <span style={{ fontSize: '0.75rem', color: item.available ? 'var(--success)' : 'var(--error)' }}>
                                            {item.available ? '● Available' : '● Booked'}
                                        </span>
                                        <button
                                            onClick={() => handleDeleteItem(item.id)}
                                            style={{ background: 'none', border: 'none', color: 'var(--error)', cursor: 'pointer', padding: '0.25rem', display: 'flex', alignItems: 'center' }}
                                            title="Delete Item"
                                        >
                                            <Trash2 size={18} />
                                        </button>
                                        <button
                                            onClick={() => handleEditItem(item)}
                                            style={{ background: 'none', border: 'none', color: 'var(--primary)', cursor: 'pointer', padding: '0.25rem', display: 'flex', alignItems: 'center' }}
                                            title="Edit Item"
                                        >
                                            <Edit size={18} />
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            ) : (
                <div className="glass-card" style={{ overflowX: 'auto' }}>
                    {(!bookings || bookings.length === 0) ? (
                        <div style={{ padding: '2rem', textAlign: 'center' }}>
                            <List size={48} color="var(--text-muted)" style={{ marginBottom: '1rem' }} />
                            <h3>No pending requests</h3>
                            <p style={{ color: 'var(--text-muted)' }}>You will see booking requests from renters here.</p>
                        </div>
                    ) : (
                        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                            <thead>
                                <tr style={{ textAlign: 'left', color: 'var(--text-muted)', fontSize: '0.9rem' }}>
                                    <th style={{ padding: '1rem' }}>Item</th>
                                    <th style={{ padding: '1rem' }}>Renter</th>
                                    <th style={{ padding: '1rem' }}>Dates</th>
                                    <th style={{ padding: '1rem' }}>Status</th>
                                    <th style={{ padding: '1rem' }}>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {bookings.map(booking => (
                                    <tr key={booking.id} style={{ borderTop: '1px solid var(--glass-border)' }}>
                                        <td style={{ padding: '1rem' }}>
                                            <div style={{ fontWeight: '600' }}>{booking.itemName || `ID: ${booking.itemId}`}</div>
                                            <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Booking #{booking.id}</div>
                                        </td>
                                        <td style={{ padding: '1rem' }}>
                                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                                <div style={{ width: '24px', height: '24px', borderRadius: '50%', background: 'var(--primary)', color: 'white', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.75rem' }}>
                                                    {booking.renterName ? booking.renterName.charAt(0).toUpperCase() : '?'}
                                                </div>
                                                {booking.renterName || 'Unknown'}
                                            </div>
                                        </td>
                                        <td style={{ padding: '1rem' }}>{booking.startDate} - {booking.endDate}</td>
                                        <td style={{ padding: '1rem' }}>
                                            <span style={{ padding: '0.25rem 0.75rem', borderRadius: '12px', fontSize: '0.75rem', background: 'var(--glass)', color: 'var(--text-muted)' }}>
                                                {booking.status}
                                            </span>
                                        </td>
                                        <td style={{ padding: '1rem' }}>
                                            {booking.status === 'REQUESTED' && (
                                                <div style={{ display: 'flex', gap: '0.5rem' }}>
                                                    <button className="btn-primary" style={{ padding: '0.4rem', borderRadius: '8px' }} onClick={() => handleBookingAction(booking.id, 'approve')}>
                                                        <Check size={16} />
                                                    </button>
                                                    <button className="btn-outline" style={{ padding: '0.4rem', borderRadius: '8px', color: 'var(--error)' }} onClick={() => handleBookingAction(booking.id, 'reject')}>
                                                        <X size={16} />
                                                    </button>
                                                </div>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}

            {showItemModal && (
                <div style={{ position: 'fixed', top: 0, left: 0, width: '100%', height: '100%', background: 'rgba(0,0,0,0.8)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 2000 }}>
                    <div className="glass-card animate-in" style={{ width: '100%', maxWidth: '500px' }}>
                        <h3>{isEditing ? 'Edit Item' : 'List New Item'}</h3>
                        <form onSubmit={handleAddItem} style={{ marginTop: '1.5rem' }}>
                            <div className="input-group">
                                <label>Item Name</label>
                                <input type="text" value={newItem.itemName} onChange={e => setNewItem({ ...newItem, itemName: e.target.value })} required />
                            </div>
                            <div className="input-group">
                                <label>Description</label>
                                <textarea rows="3" value={newItem.description} onChange={e => setNewItem({ ...newItem, description: e.target.value })} required style={{ width: '100%', background: 'var(--glass)', border: '1px solid var(--glass-border)', borderRadius: '12px', padding: '0.75rem', color: 'white' }}></textarea>
                            </div>
                            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                                <div className="input-group">
                                    <label>Price / Day</label>
                                    <input type="number" value={newItem.pricePerDay} onChange={e => setNewItem({ ...newItem, pricePerDay: e.target.value })} required />
                                </div>
                                <div className="input-group">
                                    <label>Category</label>
                                    <input
                                        type="text"
                                        value={newItem.categoryName || ''}
                                        onChange={e => setNewItem({ ...newItem, categoryName: e.target.value })}
                                        placeholder="Enter category"
                                        required
                                    />
                                </div>
                            </div>
                            <div className="input-group">
                                <label>Upload Image</label>
                                <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                                    <label className="btn-outline" style={{ flex: 1, cursor: 'pointer', textAlign: 'center', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.5rem' }}>
                                        <Upload size={18} />
                                        {newItem.imageFile ? newItem.imageFile.name : 'Choose Image'}
                                        <input
                                            type="file"
                                            accept="image/*"
                                            style={{ display: 'none' }}
                                            onChange={e => setNewItem({ ...newItem, imageFile: e.target.files[0] })}
                                        />
                                    </label>
                                    {newItem.imageFile && (
                                        <button type="button" onClick={() => setNewItem({ ...newItem, imageFile: null })} style={{ background: 'none', border: 'none', color: 'var(--error)', cursor: 'pointer' }}>
                                            <X size={18} />
                                        </button>
                                    )}
                                </div>
                            </div>
                            <div style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
                                <button type="button" className="btn-outline" style={{ flex: 1 }} onClick={closeModal}>Cancel</button>
                                <button type="submit" className="btn-primary" style={{ flex: 1 }}>{isEditing ? 'Update Item' : 'List Item'}</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default OwnerDashboard;
