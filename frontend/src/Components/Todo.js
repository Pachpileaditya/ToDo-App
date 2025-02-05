import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

const API_URL = "http://localhost:8080/task/";

function Todo() {
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState("");
  const [editingTask, setEditingTask] = useState(null);
  const [editText, setEditText] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = () => {
    axios
      .get(API_URL, { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` } })
      .then(response => setTasks(response.data))
      .catch(error => console.error("Error fetching tasks:", error));
  };

  const addTask = () => {
    if (!newTask) return;
    axios
      .post(API_URL, { task: newTask, completed: false }, { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` } })
      .then(response => {
        setTasks([...tasks, response.data]);
        setNewTask("");
      })
      .catch(error => console.error("Error adding task:", error));
  };

  const deleteTask = (id) => {
    axios
      .delete(`${API_URL}${id}`, { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` } })
      .then(() => setTasks(tasks.filter(task => task.id !== id)))
      .catch(error => console.error("Error deleting task:", error));
  };

  const startEdit = (task) => {
    setEditingTask(task);
    setEditText(task.task);
  };

  const saveEdit = () => {
    if (!editText || !editingTask) return;
    axios
      .put(`${API_URL}${editingTask.id}`, { task: editText, completed: editingTask.completed }, { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` } })
      .then(response => {
        setTasks(tasks.map(task => (task.id === editingTask.id ? response.data : task)));
        setEditingTask(null);
        setEditText("");
      })
      .catch(error => console.error("Error updating task:", error));
  };

  const toggleComplete = (task) => {
    axios
      .put(`${API_URL}${task.id}`, { ...task, completed: !task.completed }, { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` } })
      .then(response => {
        setTasks(tasks.map(t => (t.id === task.id ? response.data : t)));
      })
      .catch(error => console.error("Error updating task:", error));
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login"); // Redirect to login page
  };

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">To-Do List</h2>
      <button className="btn btn-danger mb-3" onClick={handleLogout}>Logout</button>

      <div className="input-group mb-3">
        <input
          type="text"
          className="form-control"
          placeholder="Enter new task"
          value={newTask}
          onChange={(e) => setNewTask(e.target.value)}
        />
        <button className="btn btn-primary" onClick={addTask}>Add</button>
      </div>

      <ul className="list-group">
        {tasks.map(task => (
          <li key={task.id} className="list-group-item d-flex justify-content-between align-items-center">
            <div className="d-flex align-items-center">
              <input
                type="checkbox"
                className="form-check-input me-2"
                checked={task.completed}
                onChange={() => toggleComplete(task)}
              />
              {editingTask?.id === task.id ? (
                <>
                  <input
                    className="form-control me-2"
                    value={editText}
                    onChange={(e) => setEditText(e.target.value)}
                  />
                  <button className="btn btn-success btn-sm me-2" onClick={saveEdit}>Save</button>
                  <button className="btn btn-secondary btn-sm" onClick={() => setEditingTask(null)}>Cancel</button>
                </>
              ) : (
                <span className={task.completed ? "text-decoration-line-through" : ""}>{task.task}</span>
              )}
            </div>
            {!editingTask?.id && (
              <div>
                <button className="btn btn-warning btn-sm me-2" onClick={() => startEdit(task)}>Edit</button>
                <button className="btn btn-danger btn-sm" onClick={() => deleteTask(task.id)}>Delete</button>
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Todo;
