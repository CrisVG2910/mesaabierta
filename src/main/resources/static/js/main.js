'use strict';

document.addEventListener('DOMContentLoaded', () => {
  const toggle = document.querySelector('[data-menu-toggle]');
  const menu = document.getElementById('main-menu');
  if (toggle && menu) {
    toggle.addEventListener('click', () => {
      const expanded = toggle.getAttribute('aria-expanded') === 'true';
      toggle.setAttribute('aria-expanded', String(!expanded));
      menu.classList.toggle('is-open', !expanded);
    });
    menu.querySelectorAll('a').forEach(link => link.addEventListener('click', () => {
      toggle.setAttribute('aria-expanded', 'false');
      menu.classList.remove('is-open');
    }));
  }

  // Se conserva una ilustracion local si el navegador no puede cargar la fotografia remota.
  document.querySelectorAll('img[data-fallback]').forEach(img => {
    const useFallback = () => {
      const fallback = img.dataset.fallback;
      if (fallback && img.getAttribute('src') !== fallback) img.setAttribute('src', fallback);
    };
    img.addEventListener('error', useFallback, { once: true });
    if (img.complete && img.naturalWidth === 0) useFallback();
  });
});
