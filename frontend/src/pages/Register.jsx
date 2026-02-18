import React, { useState } from 'react';
import AuthService from '../services/authService';
import { useNavigate, Link } from 'react-router-dom';
import { UserPlus, AlertCircle, CheckCircle } from 'lucide-react';

const Register = () => {
    const [formData, setFormData] = useState({
        userName: '',
        userEmail: '',
        phNo: '',
        password: '',
        role: 'RENTER'
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await AuthService.register(formData);
            setSuccess(true);
            setTimeout(() => navigate('/login'), 2000);
        } catch (err) {
            setError(err.response?.data?.message || 'Registration failed. Try again.');
        }
    };

    if (success) {
        return (
            <div className="container" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh' }}>
                <div className="glass-card animate-in" style={{ textAlign: 'center', maxWidth: '400px' }}>
                    <CheckCircle color="var(--success)" size={64} style={{ marginBottom: '1rem' }} />
                    <h2>Registration Successful!</h2>
                    <p style={{ color: 'var(--text-muted)', marginTop: '0.5rem' }}>Redirecting to login...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="container" style={{ display: 'flex', justifyContent: 'center', padding: '2rem 0' }}>
            <div className="glass-card animate-in" style={{ width: '100%', maxWidth: '500px' }}>
                <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
                    <div style={{ background: 'rgba(99, 102, 241, 0.1)', width: '64px', height: '64px', borderRadius: '16px', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1rem' }}>
                        <UserPlus color="var(--primary)" size={32} />
                    </div>
                    <h2>Create Account</h2>
                    <p style={{ color: 'var(--text-muted)', marginTop: '0.5rem' }}>Join RentIt today and start managing assets</p>
                </div>

                {error && (
                    <div style={{ background: 'rgba(239, 68, 68, 0.1)', color: 'var(--error)', padding: '1rem', borderRadius: '12px', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.9rem' }}>
                        <AlertCircle size={18} /> {error}
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    <div className="input-group">
                        <label>Full Name</label>
                        <input name="userName" type="text" placeholder="John Doe" onChange={handleChange} required />
                    </div>
                    <div className="input-group">
                        <label>Email Address</label>
                        <input name="userEmail" type="email" placeholder="john@example.com" onChange={handleChange} required />
                    </div>
                    <div className="input-group">
                        <label>Phone Number</label>
                        <input name="phNo" type="text" placeholder="+1 234 567 890" onChange={handleChange} required />
                    </div>
                    <div className="input-group">
                        <label>Password</label>
                        <input name="password" type="password" placeholder="••••••••" onChange={handleChange} required />
                    </div>
                    <div className="input-group">
                        <label>I want to be a:</label>
                        <select name="role" onChange={handleChange} value={formData.role}>
                            <option value="RENTER">Renter (Browse and rent items)</option>
                            <option value="OWNER">Owner (List and manage items)</option>
                        </select>
                    </div>
                    <button type="submit" className="btn-primary" style={{ width: '100%', marginTop: '1rem' }}>
                        Create Account
                    </button>
                </form>

                <p style={{ textAlign: 'center', marginTop: '1.5rem', color: 'var(--text-muted)', fontSize: '0.9rem' }}>
                    Already have an account? <Link to="/login" style={{ color: 'var(--primary)', textDecoration: 'none', fontWeight: '600' }}>Sign in here</Link>
                </p>
            </div>
        </div>
    );
};

export default Register;
