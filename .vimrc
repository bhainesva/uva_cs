" Allows vim features not compatible with vi
set nocompatible

"shows commands while typing
set showcmd

"Autoindent
set autoindent

"spaces not tabs
set expandtab
set smarttab

"Reasonable length tabs
set shiftwidth=4
set softtabstop=4

"Mouse support
set mouse=a

"Line numbers
set number

"Edit vimrc \ev
nnoremap <silent><Leader>gv :tabnew<CR>:e ~/.vimrc<CR>

" Move tabs with alt + left|right
nnoremap <silent> <A-Left> :execute 'silent! tabmove ' . (tabpagenr()-2)<CR>
nnoremap <silent> <A-Right> :execute 'silent! tabmove ' . tabpagenr()<CR>

"Automatically cds to the working directory
set autochdir
" Prevents creation of temporary files
set nobackup
set noswapfile

" Ignoring case in searches
set ignorecase

" Unless you use a capital letter
set smartcase

" Remap jj to escape in insert mode. Was 'ii' but 'ascii' is hard to type then.
inoremap jj <Esc>

" Incremental searching is sexy
set incsearch

" Highlight things that we find with the search
set hlsearch

" Set off the other paren
highlight MatchParen ctermbg=4

" New Tab
nnoremap <silent> <C-t> :tabnew<CR>

" Up and down are more logical with g..
nnoremap <silent> k gk
nnoremap <silent> j gj
inoremap <silent> <Up> <Esc>gka
inoremap <silent> <Down> <Esc>gja

" Swap ; and :  Convenient.
nnoremap ; :
nnoremap : ;

"Colorscheme
colorscheme slate

"Highlight current line
set cul
hi CursorLine term=none cterm=none ctermbg=7
