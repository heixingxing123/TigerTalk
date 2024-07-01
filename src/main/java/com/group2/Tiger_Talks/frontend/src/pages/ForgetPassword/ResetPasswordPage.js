import React, {useState} from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import axios from 'axios';
import "../../assets/styles/ForgetPassword/ResetPasswordPage.css";

const ResetPasswordPage = () => {
    const {state} = useLocation();
    const {email} = state || {};
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();

    const handleChange = (e) => {
        const {name, value} = e.target;
        if (name === 'newPassword') {
            setNewPassword(value);
        } else if (name === 'confirmPassword') {
            setConfirmPassword(value);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrors({});
        if (newPassword !== confirmPassword) {
            setErrors({confirmPassword: 'Passwords do not match!'});
            return;
        }
        try {
            const response = await axios.post('http://localhost:8085/api/passwordReset/resetPassword', {
                email,
                password: newPassword,
            });
            alert(response.data);
            navigate('/');
        } catch (error) {
            setErrors({general: error.response.data});
        }
    };

    return (
        <div className="reset-password">
            <div className="reset-password-card">
                <h1>Reset Password</h1>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>New Password</label>
                        <input
                            type="password"
                            name="newPassword"
                            value={newPassword}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label>Confirm Password</label>
                        <input
                            type="password"
                            name="confirmPassword"
                            value={confirmPassword}
                            onChange={handleChange}
                        />
                    </div>
                    <button type="submit">Submit</button>
                </form>
                {errors.confirmPassword && <p className="error-css">{errors.confirmPassword}</p>}
                {errors.general && <p className="error-css">{errors.general}</p>}
            </div>
        </div>
    );
};

export default ResetPasswordPage;