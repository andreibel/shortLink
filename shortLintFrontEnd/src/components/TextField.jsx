/**
 * TextField component renders a labeled input field with validation and error display.
 * Designed for use with React Hook Form's `register` function.
 *
 * @param {Object} props - Component props.
 * @param {string} props.label - The label text for the input field.
 * @param {string} props.id - The unique identifier for the input field.
 * @param {string} props.type - The input type (e.g., "text", "email", "url").
 * @param {Object} props.errors - Validation errors object from React Hook Form.
 * @param {function} props.register - React Hook Form's register function for input registration.
 * @param {boolean} props.required - Whether the field is required.
 * @param {string} props.message - Custom validation message for required field.
 * @param {string} [props.className] - Optional additional CSS classes for styling.
 * @param {number} [props.min] - Minimum length for input validation.
 * @param {string|number} [props.value] - Input value (for controlled input).
 * @param {string} [props.placeholder] - Placeholder text for the input field.
 *
 * @returns {JSX.Element} A labeled input field with validation and error message.
 */
const TextField = ({
                     label, id, type, errors, register, required, message, className, min, value, placeholder,
                   }) => {
  return (<div className="flex flex-col gap-1">
    <label
      htmlFor={id}
      className={`${className ? className : ""} font-semibold text-md  `}
    >
      {label}
    </label>

    <input
      type={type}
      id={id}
      placeholder={placeholder}
      className={`${className ? className : ""} px-2 py-2 border   outline-none bg-transparent  text-slate-700 rounded-md ${errors[id]?.message ? "border-red-500" : "border-slate-600"}`}
      {...register(id, {
        required: {value: required, message},
        minLength: min ? {value: min, message: "Minimum 6 character is required"} : null,

        pattern: type === "email" ? {
          value: /^[a-zA-Z0-9]+@(?:[a-zA-Z0-9]+\.)+com+$/, message: "Invalid email",
        } : type === "url" ? {
          value: /^(https?:\/\/)?(([a-zA-Z0-9\u00a1-\uffff-]+\.)+[a-zA-Z\u00a1-\uffff]{2,})(:\d{2,5})?(\/[^\s]*)?$/,
          message: "Please enter a valid url",
        } : null,
      })}
    />

    {errors[id]?.message && (<p className="text-sm font-semibold text-red-600 mt-0">
      {errors[id]?.message}*
    </p>)}
  </div>);
};

export default TextField;