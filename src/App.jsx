import { Route, Routes } from "react-router";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";

function App() {
  return (
    <Routes>
      {/* change the path from "/" to "/signUp" on merging with another tasks */}
      <Route path="/" element={<SignupPage />} />
      <Route path="/logIn" element={<LoginPage />} />
    </Routes>
  );
}

export default App;
