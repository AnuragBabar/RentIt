import { useState, useEffect, useCallback } from 'react';
import ItemService from '../services/itemService';

export const useItems = (ownerId = null) => {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchItems = useCallback(async () => {
        setLoading(true);
        try {
            let data;
            if (ownerId) {
                data = await ItemService.getItemsByOwner(ownerId);
            } else {
                data = await ItemService.getAvailableItems();
            }
            setItems(data);
            setError(null);
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    }, [ownerId]);

    useEffect(() => {
        fetchItems();
    }, [fetchItems]);

    return { items, loading, error, refetch: fetchItems };
};
