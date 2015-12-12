import xml.etree.ElementTree
from lxml import etree
import urllib

class Client(object):
    def __init__(self, appid):
        self.appid = appid
        self.format = "mathml"

    def mathml_to_latex(self, someXML):
        xslt_tree = etree.parse("./mmltex.xsl")
        transform = etree.XSLT(xslt_tree)
        result = transform(someXML)
        return result

    def alternate_forms(self, start_form):
        query = urllib.urlencode(dict(
            input=start_form,
            appid=self.appid,
            format=self.format
        ))

        url = 'http://api.wolframalpha.com/v2/query?' + query
        tree = etree.parse(urllib.urlopen(url))
        #tree = xml.etree.ElementTreeetree.parse(url)
        l = tree.xpath("//pod[contains(@title, 'Alternate form')]")[0]
        NSMAP = {'mw':'http://www.w3.org/1998/Math/MathML'}
        subpods = l.findall('.//mw:math', namespaces=NSMAP)
        forms = []
        for pod in subpods:
            forms.append(self.mathml_to_latex(pod))

        return forms

inputquery = "(x^2 - 4)(x+1)(4 - y)"
appid = "834JUH-LUV8EVJ928"

if __name__ == "__main__":
    converter = Client(appid)
    new_forms = converter.alternate_forms(inputquery)

    for form in new_forms:
        print form
