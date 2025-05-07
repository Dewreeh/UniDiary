
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';


export const request = async (endpoint, method = 'GET', body = null) => {
  const headers = {
    'Content-Type': 'application/json',
  };

  const options = {
    method,
    headers,
  };

  if (body) {
    options.body = JSON.stringify(body);
  }

  try {
    const response = await fetch(`${API_URL}${endpoint}`, options);
    
    const responseData = await response.json(); 
    
    if (!response.ok) {
    
      const errorMessage = responseData.message || response.statusText;
      alert(errorMessage);
      throw new Error(errorMessage);
    }
    
    return responseData;
    
  } catch (error) {
    const errorMessage = error.message || 'Неизвестная ошибка при выполнении запроса';
    throw new Error(errorMessage);
  }
};
