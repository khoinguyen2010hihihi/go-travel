// assets/js/forgot-password.js
let generatedOtp = null;
let userEmail = null;

document.addEventListener('DOMContentLoaded', () => {
  const sendOtpBtn = document.getElementById('sendOtpBtn');
  const verifyOtpBtn = document.getElementById('verifyOtpBtn');
  const resetPasswordBtn = document.getElementById('resetPasswordBtn');
  const forgotEmailInput = document.getElementById('forgotEmail');
  const otpInput = document.getElementById('otpInput');
  const newPasswordInput = document.getElementById('newPassword');
  const confirmNewPasswordInput = document.getElementById('confirmNewPassword');
  const forgotEmailMessage = document.getElementById('forgotEmailMessage');
  const otpMessage = document.getElementById('otpMessage');
  const newPasswordMessage = document.getElementById('newPasswordMessage');
  const confirmNewPasswordMessage = document.getElementById('confirmNewPasswordMessage');

  const forgotPasswordForm = document.getElementById('forgotPasswordForm');
  const otpForm = document.getElementById('otpForm');
  const resetPasswordForm = document.getElementById('resetPasswordForm');

  // Gửi OTP
  sendOtpBtn.addEventListener('click', () => {
    const email = forgotEmailInput.value.trim();
    if (!isValidEmail(email)) {
      forgotEmailMessage.textContent = 'Vui lòng nhập email hợp lệ';
      return;
    }

    userEmail = email;
    generatedOtp = generateOtp();
    
    // Giả lập gửi OTP qua email
    sendOtpEmail(email, generatedOtp).then(() => {
      forgotEmailMessage.textContent = 'OTP đã được gửi đến email của bạn';
      forgotEmailMessage.style.color = 'green';
      forgotPasswordForm.classList.add('hidden');
      otpForm.classList.remove('hidden');
    }).catch(() => {
      forgotEmailMessage.textContent = 'Lỗi khi gửi OTP, vui lòng thử lại';
    });
  });

  // Xác nhận OTP
  verifyOtpBtn.addEventListener('click', () => {
    const enteredOtp = otpInput.value.trim();
    if (enteredOtp === generatedOtp) {
      otpMessage.textContent = 'OTP hợp lệ';
      otpMessage.style.color = 'green';
      otpForm.classList.add('hidden');
      resetPasswordForm.classList.remove('hidden');
    } else {
      otpMessage.textContent = 'OTP không đúng, vui lòng thử lại';
    }
  });

  // Đặt lại mật khẩu
  resetPasswordBtn.addEventListener('click', () => {
    const newPassword = newPasswordInput.value.trim();
    const confirmNewPassword = confirmNewPasswordInput.value.trim();

    if (newPassword.length < 6) {
      newPasswordMessage.textContent = 'Mật khẩu phải có ít nhất 6 ký tự';
      return;
    }

    if (newPassword !== confirmNewPassword) {
      confirmNewPasswordMessage.textContent = 'Mật khẩu không khớp';
      return;
    }

    // Giả lập cập nhật mật khẩu
    updatePassword(userEmail, newPassword).then(() => {
      alert('Mật khẩu đã được đặt lại thành công! Bạn sẽ được chuyển về trang đăng nhập.');
      window.location.href = 'trang-chu.html#login';
    }).catch(() => {
      confirmNewPasswordMessage.textContent = 'Lỗi khi đặt lại mật khẩu, vui lòng thử lại';
    });
  });

  // Hàm kiểm tra email hợp lệ
  function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  // Hàm tạo OTP ngẫu nhiên
  function generateOtp() {
    return Math.floor(100000 + Math.random() * 900000).toString();
  }

  // Hàm giả lập gửi email OTP
  function sendOtpEmail(email, otp) {
    return new Promise((resolve) => {
      console.log(`Gửi OTP ${otp} đến ${email}`);
      setTimeout(resolve, 1000); // Giả lập thời gian gửi
    });
  }

  // Hàm giả lập cập nhật mật khẩu
  function updatePassword(email, newPassword) {
    return new Promise((resolve) => {
      console.log(`Cập nhật mật khẩu cho ${email} thành ${newPassword}`);
      setTimeout(resolve, 1000); // Giả lập thời gian cập nhật
    });
  }

  // Xử lý nút show/hide mật khẩu
  const showHideButtons = document.querySelectorAll('.show-hide-btn');
  showHideButtons.forEach(button => {
    button.addEventListener('click', () => {
      const input = button.previousElementSibling;
      if (input.type === 'password') {
        input.type = 'text';
        button.src = 'assets/img/icon/eye-off.svg';
      } else {
        input.type = 'password';
        button.src = 'assets/img/icon/eye.svg';
      }
    });
  });
});