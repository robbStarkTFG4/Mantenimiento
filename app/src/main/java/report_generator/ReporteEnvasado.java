package report_generator;

/**
 * Created by marcoisaac on 6/6/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import local_Db.EquipoDB;
import local_Db.FotoDB;
import local_Db.HistorialDetallesDB;
import local_Db.LugarDB;
import local_Db.OrdenDB;


/**
 * @author marcoisaac
 */
public class ReporteEnvasado {


    private Context context;
    private OrdenDB orden;
    private final List<HistorialDetallesDB> observaciones = new ArrayList<>();
    private List<FotoDB> fotos;
    private EquipoDB equipo;
    private LugarDB lugar;
    private byte[] imageBytes;

    public ReporteEnvasado(Context context, OrdenDB orden, List<HistorialDetallesDB> observaciones, List<FotoDB> fotos, EquipoDB equipo, LugarDB lugar, byte[] imageBytes) throws IOException, FileNotFoundException, DocumentException {

        this.context = context;
        this.orden = orden;
        this.equipo = equipo;
        this.lugar = lugar;
        this.fotos = fotos;
        this.imageBytes = imageBytes;

        if (observaciones != null) {
            for (HistorialDetallesDB hs : observaciones) {
                if (hs.getValor() != null) {
                    if (hs.getValor().length() > 0) {
                        this.observaciones.add(hs);
                    }
                }
            }
        }


        buildReport();
    }

    private void buildReport() throws FileNotFoundException, IOException, DocumentException {
        Document document = new Document(new Rectangle(800, 700), 7f, 7f, 50f, 7f);

        File reporteDir = new File(Environment.getExternalStorageDirectory() + "/reportesMim");

        if (!reporteDir.exists()) {
            reporteDir.mkdir();
        }

        String nombre;
        if (orden.getNumeroOrden() == null) {
            nombre = orden.getActividad();
        } else {
            nombre = orden.getNumeroOrden();
        }

        PdfWriter.getInstance(document,
                new FileOutputStream(Environment.  //THIS WORKS
                        getExternalStorageDirectory() + "/reportesMim" + "/" + nombre + ".pdf"));


        document.open();


        PdfPTable table = new PdfPTable(8);

        //LEFT HEADER CONTENT
        PdfPTable leftHeaderTable = new PdfPTable(4);

        PdfPCell imgCell = new PdfPCell();
        imgCell.setBorder(Rectangle.NO_BORDER);
        imgCell.setPaddingTop(14);
        imgCell.setColspan(1);
        imgCell.setFixedHeight(25);

        Image img = Image.getInstance(imageBytes);

        imgCell.addElement(img);

        PdfPCell reportTitleCell = new PdfPCell(new Paragraph("REPORTE MANTENIMIENTO"));
        reportTitleCell.setPaddingTop(14);
        reportTitleCell.setPaddingLeft(20);
        reportTitleCell.setColspan(3);
        reportTitleCell.setBorder(Rectangle.NO_BORDER);

        leftHeaderTable.addCell(imgCell);
        leftHeaderTable.addCell(reportTitleCell);

        PdfPCell leftHeaderMainCell = new PdfPCell(leftHeaderTable);
        leftHeaderMainCell.setColspan(4);

        //END CONTENT
        //RIGHT HEADER WITH INFO ABOUT ORDER AND DATE
        PdfPTable infHeader = new PdfPTable(3);

        PdfPCell numberOrderLabel = new PdfPCell(new Paragraph("#ORDEN"));
        numberOrderLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        numberOrderLabel.setColspan(1);

        PdfPCell numberOrderValue = new PdfPCell(new Paragraph(orden.getNumeroOrden()));
        numberOrderValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        numberOrderValue.setColspan(2);

        PdfPCell prioridadLabel = new PdfPCell(new Paragraph("PRIORIDAD"));
        prioridadLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        prioridadLabel.setColspan(1);

        PdfPCell prioridadValue = new PdfPCell(new Paragraph(orden.getPrioridad()));
        prioridadValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        prioridadValue.setColspan(2);

        PdfPCell fechaLabel = new PdfPCell(new Paragraph("FECHA"));
        fechaLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        fechaLabel.setColspan(1);
        //dd-MM-yyyy
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String fecha = format1.format(new Date());

        PdfPCell fechaValue = new PdfPCell(new Paragraph(fecha));
        fechaValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        fechaValue.setColspan(2);

        infHeader.addCell(numberOrderLabel);
        infHeader.addCell(numberOrderValue);
        infHeader.addCell(prioridadLabel);
        infHeader.addCell(prioridadValue);
        infHeader.addCell(fechaLabel);
        infHeader.addCell(fechaValue);

        PdfPCell cellHeaderRight = new PdfPCell(infHeader);
        cellHeaderRight.setColspan(4);
        //END HEADER

        PdfPCell areaLabel = new PdfPCell(new Paragraph("AREA"));
        areaLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        areaLabel.setVerticalAlignment(Element.ALIGN_CENTER);
        areaLabel.setFixedHeight(30);
        areaLabel.setPaddingTop(5);
        areaLabel.setColspan(2);

        PdfPCell actividadLabel = new PdfPCell(new Paragraph("ACTIVIDAD"));
        actividadLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        actividadLabel.setFixedHeight(30);
        actividadLabel.setVerticalAlignment(Element.ALIGN_CENTER);
        actividadLabel.setPaddingTop(5);
        actividadLabel.setColspan(3);

        PdfPCell responsableLabel = new PdfPCell(new Paragraph("RESPONSABLE DE OPERACION"));
        responsableLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        responsableLabel.setFixedHeight(30);
        responsableLabel.setVerticalAlignment(Element.ALIGN_CENTER);
        responsableLabel.setPaddingTop(5);
        responsableLabel.setColspan(3);

        String area;
        if (lugar.getNombre().contains("linea")) {
            area = "envasado";
        } else {
            area = "elaboracion";
        }

        PdfPCell areaValor = new PdfPCell(new Paragraph(area));
        areaValor.setFixedHeight(25);
        areaValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        areaValor.setColspan(2);

        PdfPCell actividadValor = new PdfPCell(new Paragraph(orden.getActividad()));
        actividadValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        actividadValor.setFixedHeight(25);
        actividadValor.setColspan(3);

        PdfPCell responsableValor = new PdfPCell(new Paragraph(orden.getEncargado()));
        responsableValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        responsableValor.setFixedHeight(25);
        responsableValor.setColspan(3);

        // 2 FILAS PARA INF. EQUIPO Y LUGAR
        PdfPCell equipoLabel = new PdfPCell(new Paragraph("EQUIPO/CONJUNTO"));
        equipoLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        equipoLabel.setVerticalAlignment(Element.ALIGN_CENTER);
        equipoLabel.setFixedHeight(30);
        equipoLabel.setPaddingTop(5);
        equipoLabel.setColspan(4);

        PdfPCell lugarLabel = new PdfPCell(new Paragraph("LUGAR"));
        lugarLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        lugarLabel.setFixedHeight(30);
        lugarLabel.setVerticalAlignment(Element.ALIGN_CENTER);
        lugarLabel.setPaddingTop(5);
        lugarLabel.setColspan(4);

        PdfPCell equipoValor = new PdfPCell(new Paragraph(equipo.getNumeroEquipo()));
        equipoValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        equipoValor.setFixedHeight(25);
        equipoValor.setColspan(4);

        PdfPCell lugarValor = new PdfPCell(new Paragraph(lugar.getNombre()));
        lugarValor.setHorizontalAlignment(Element.ALIGN_CENTER);
        lugarValor.setFixedHeight(25);
        lugarValor.setColspan(4);

        //END INFO EQUIPO
        // 4 ROW and 5 ROW
        PdfPCell descripcionLabel = new PdfPCell(new Paragraph("DESCRIPCION"));
        descripcionLabel.setPadding(12);
        descripcionLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        descripcionLabel.setColspan(8);

        PdfPCell descripcionValor = new PdfPCell(new Paragraph(orden.getDescripcion()));
        descripcionValor.setPadding(10);
        descripcionValor.setColspan(8);
        //END ROWS

        //ROW BEFORE HISTORIAL_DETALLES
        PdfPCell historialLabel = new PdfPCell(new Paragraph("OBSERVACIONES"));
        historialLabel.setPadding(12);
        historialLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        historialLabel.setColspan(8);
        //END HISTORIAL

        table.addCell(leftHeaderMainCell);
        table.addCell(cellHeaderRight);
        table.addCell(areaLabel);
        table.addCell(actividadLabel);
        table.addCell(responsableLabel);

        table.addCell(areaValor);
        table.addCell(actividadValor);
        table.addCell(responsableValor);
        table.addCell(equipoLabel);
        table.addCell(lugarLabel);
        table.addCell(equipoValor);
        table.addCell(lugarValor);
        table.addCell(descripcionLabel);
        table.addCell(descripcionValor);
        if (observaciones.size() > 0) {
            table.addCell(historialLabel);

            //LOOP HISTORIAL_DETALLES
            for (int i = 0; i < observaciones.size(); i++) {
                HistorialDetallesDB historial = observaciones.get(i);

                PdfPCell paramCell = new PdfPCell();
                paramCell.setColspan(3);
                paramCell.addElement(new Paragraph(historial.getParametro()));
                paramCell.setVerticalAlignment(Element.ALIGN_CENTER);
                paramCell.setPaddingLeft(10);
                paramCell.setPaddingBottom(10);

                PdfPCell valueParamCell = new PdfPCell();
                valueParamCell.setColspan(5);
                valueParamCell.setPaddingLeft(10);
                valueParamCell.setVerticalAlignment(Element.ALIGN_CENTER);
                valueParamCell.addElement(new Paragraph(historial.getValor()));
                valueParamCell.setPaddingBottom(10);

                table.addCell(paramCell);
                table.addCell(valueParamCell);
            }
            //END LOOP HISTORIAL
        }
        // FIRST ROWS OF FOTOGRAPHIC REPORT

        PdfPTable table2 = new PdfPTable(8);

        if (fotos != null) {
            PdfPCell pasoLabel = new PdfPCell(new Paragraph("PASO"));
            pasoLabel.setFixedHeight(20);
            pasoLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
            pasoLabel.setColspan(1);

            PdfPCell accionLabel = new PdfPCell(new Paragraph("ACCION"));
            accionLabel.setFixedHeight(20);
            accionLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
            accionLabel.setColspan(3);

            PdfPCell imagenLabel = new PdfPCell(new Paragraph("IMAGENES"));
            imagenLabel.setFixedHeight(20);
            imagenLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
            imagenLabel.setColspan(4);
            //END ROWS


            //ROW BEFORE HISTORIAL_DETALLES
            PdfPCell headerPictures = new PdfPCell(new Paragraph("PROCEDIMIENTO"));
            headerPictures.setPadding(12);
            headerPictures.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerPictures.setColspan(8);

            table2.addCell(headerPictures);
            //END HISTORIAL

            table2.addCell(pasoLabel);
            table2.addCell(accionLabel);
            table2.addCell(imagenLabel);

            //fotos loop
            for (int i = 0; i < fotos.size(); i++) {
                FotoDB foto = fotos.get(i);
                PdfPCell pasoVal = new PdfPCell(new Paragraph(String.valueOf(i)));
                pasoVal.setHorizontalAlignment(Element.ALIGN_CENTER);
                pasoVal.setColspan(1);

                PdfPCell detail = new PdfPCell(new Paragraph(foto.getTitulo()));
                detail.setPadding(5);
                detail.setBorder(Rectangle.NO_BORDER);

                PdfPCell accionVal = new PdfPCell();
                accionVal.addElement(new Paragraph(foto.getDescripcion()));
                accionVal.setHorizontalAlignment(Element.ALIGN_CENTER);
                accionVal.setBorder(Rectangle.NO_BORDER);
                //accionVal.setColspan(3);

                PdfPTable infoTable = new PdfPTable(1);
                infoTable.addCell(detail);
                infoTable.addCell(accionVal);

                PdfPCell infiCell = new PdfPCell();
                infiCell.setColspan(3);
                infiCell.addElement(infoTable);

                File file = new File(foto.getArchivo());

                /*InputStream st = new BufferedInputStream(new FileInputStream(file));
                Bitmap bmp = BitmapFactory.decodeStream(st);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);*/
                Image imgFoto = Image.getInstance(foto.getArchivo());
                //Image imgFoto = Image.getInstance(stream.toByteArray());

                PdfPTable imagenTable = new PdfPTable(1);

                PdfPCell fotoCell = new PdfPCell();
                fotoCell.setColspan(1);
                fotoCell.addElement(imgFoto);
                fotoCell.setFixedHeight(310);
                fotoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                fotoCell.setBorder(Rectangle.NO_BORDER);

                imagenTable.addCell(fotoCell);

                PdfPCell imagenVal = new PdfPCell();
                imagenVal.setColspan(4);
                imagenVal.addElement(imagenTable);

                table2.addCell(pasoVal);
                table2.addCell(infiCell);
                table2.addCell(imagenVal);
            }
            //end loop
        }

        //table.addCell(tiempoLabel);
        document.add(table);
        document.newPage();

        if (fotos != null) {
            document.add(table2);
        }

        document.close();
    }

}
