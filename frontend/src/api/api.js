const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export const request = async (endpoint, method = 'GET', body = null, requiresAuth = true) => {
  const headers = {
    'Content-Type': 'application/json',
  };


  if (requiresAuth) {
    const token = localStorage.getItem('accessToken');
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    } else {
      console.warn('Токен доступа не найден в localStorage');

    }
  }

  const options = {
    method,
    headers,
  };

  if (body) {
    options.body = JSON.stringify(body);
  }

  try {
    const response = await fetch(`${API_URL}${endpoint}`, options);
    
    if (response.status === 401) {
      alert("Неправильный логин или пароль");
      return;
    } else if (response.status === 403){
      alert("Доступ запрещён");
      window.location.href = '/';
      return;
    }

    const responseData = await response.json();
    
    if (!response.ok) {
      const errorMessage = responseData.message || response.statusText;
      alert(errorMessage);
      throw new Error(errorMessage);
    }
    
    return responseData;
    
  } catch (error) {
    const errorMessage = error.message || 'Неизвестная ошибка при выполнении запроса';
    console.error('Ошибка запроса:', errorMessage);
    throw error;
  }
};