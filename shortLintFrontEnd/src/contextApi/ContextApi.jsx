import {createContext, useContext, useState} from "react";

/**
 * React Context for global state management.
 * Provides access to the authentication token and its setter.
 * @type {React.Context<{token: string|null, setToken: function}>}
 */
const ContextApi = createContext();

/**
 * ContextProvider component to wrap the app and provide context values.
 * Retrieves JWT token from localStorage on initialization.
 *
 * @param {Object} props
 * @param {React.ReactNode} props.children - Child components to be wrapped by the provider.
 * @returns {JSX.Element} Context provider with token state and setter.
 */
export const ContextProvider = ({children}) => {
  // Retrieve JWT token from localStorage, or null if not present
  const getToken = localStorage.getItem("JWT_TOKEN") ? JSON.parse(localStorage.getItem("JWT_TOKEN")) : null;

  // State to hold the JWT token
  const [token, setToken] = useState(getToken);

  // Object containing state and setter to be provided via context
  const sendData = {
    token, setToken,
  };

  return <ContextApi.Provider value={sendData}>{children}</ContextApi.Provider>
};

/**
 * Custom hook to access the context values.
 * @returns {{token: string|null, setToken: function}} Context values.
 */
  // eslint-disable-next-line react-refresh/only-export-components
export const useStoreContext = () => {
    const context = useContext(ContextApi);
    return context;
  }