/**
 * Verifies a string for only containing letters.
 * Returns a boolean representing if the verification was successful.
 *
 * @param str         String to be checked
 * @returns {boolean} A boolean representing the status of the verification
 */
export function verifyStringForLetters(str: string) {
  const regex = /^[\p{L}]+$/u;
  return regex.test(str.trim());
}

/**
 * Verifies a string for only containing numbers.
 * Returns a boolean representing if the verification was successful.
 *
 * @param str         String to be checked
 * @returns {boolean} A boolean representing the status of the verification
 */
export function verifyStringForNumbers(str: string) {
  const regex = /^[0-9]+$/u;
  return regex.test(str.trim());
}

/**
 * Verifies a string to be an email.
 * Returns a boolean representing if the verification was successful.
 *
 * @param str         String to be checked
 * @returns {boolean} A boolean representing the status of the verification
 */
export function verifyStringForEmail(str: string) {
  const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return regex.test(str.trim());
}

/**
 * Verifies the  during the login process.
 * Returns a boolean representing if the verification was successful
 *
 * @param str         String to be checked
 * @returns {boolean} A boolean representing the status of the verification
 */
export function verifyStringNotEmpty(str: string) {
  return (str.trim() !== '')
}

