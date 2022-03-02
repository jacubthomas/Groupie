const defaultTheme = require('tailwindcss/defaultTheme')
module.exports = {
  purge: ['./src/**/*.{js,jsx,ts,tsx}', './public/index.html'],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      colors: {
        themePrimary: {
          DEFAULT: "#990000",
        },
        themeSecondary: {
          DEFAULT: "#FFCC00",
        },
        gray: {
          25: '#f4f4f4',
          30: '#F5F5F5',
          32: '#F6F6F6',
          35: '#f8f9fa',
          40: '#f9f9f9',
          45: '#fafafa',
          46: '#fcfcfc',
          50: '#d1d5db',
          60: '#909090',
          '60h': '#858588',
          70: '#939090',
          80: '#464648',
          85: '#ECECEC',
          90: '#eeeeee',
          95: '#979797',
          100: '#A8A2A2',
          130: '#AFAFB0',
          150: '#C2C2C2',
          200: '#C4C4C4',
          300: '#676262',
          400: '#E5E5E5',
          500: '#6b7280',
          510: '#6D6B6B',
          600: '#4b5563',
          750: '#39393A',
        }
      },
      minHeight: {
        '100': '25rem',
      }
    },
    boxShadow: {
      sm: '0 1px 2px 0 rgba(0, 0, 0, 0.05)',
      DEFAULT: '0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)',
      md: '0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)',
      lg: '0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)',
      xl: '0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)',
      '2xl': '0 25px 50px -12px rgba(0, 0, 0, 0.25)',
      '3xl': '0 35px 60px -15px rgba(0, 0, 0, 0.3)',
      inner: 'inset 0 2px 4px 0 rgba(0, 0, 0, 0.06)',
      none: 'none',
    },
    screens: {
      'xs': '475px',
      ...defaultTheme.screens,
    },
  },
  variants: {
    extend: {
      opacity: ["disabled"],
    },
  },
  plugins: [],
}
