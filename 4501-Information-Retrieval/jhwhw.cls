%=====================================================================
% jhwhw.cls
% Provide jhwhw.cls class
%=====================================================================

%=====================================================================
% Identification
%=====================================================================
\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{jhwhw}[2009/02/11 Justin Wilson's Homework Class]

\LoadClass[letterpaper, 12pt]{article}
%\RequirePackage{jhwmath} Personal style file I use.

\RequirePackage{fancyhdr}
\RequirePackage[top=1in,bottom=1in,left=1in,right=1in]{geometry}
\RequirePackage{graphicx}
\RequirePackage{empheq}
\RequirePackage{ifthen}
%\RequirePackage{jhwgraphics} Another personal style file I use.


%=====================================================================
% Commands
%=====================================================================

  \setlength{\headheight}{15pt}
  \lhead{\@author}\chead{\@title}\rhead{\today}
  \lfoot{}\cfoot{\thepage}\rfoot{}
  \pagestyle{fancy}

\ifx\pdfoutput\undefined                         %LaTeX
  \RequirePackage[ps2pdf,bookmarks=true]{hyperref}
  \hypersetup{ %
    pdfauthor   = {\@author},
    pdftitle    = {\@title},
    pdfcreator  = {LaTeX with hyperref package},
    pdfproducer = {dvips + ps2pdf}
  }
\else                                            %PDFLaTeX
  \RequirePackage[pdftex,bookmarks=true]{hyperref}
  \hypersetup{ %
    pdfauthor   = {\@author},
    pdftitle    = {\@title},
    pdfcreator  = {LaTeX with hyperref package},
    pdfproducer = {dvips + ps2pdf}
  }
\pdfadjustspacing=1
\fi
